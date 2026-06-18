package com.crm.springmvc.controller;

import com.crm.springmvc.entity.Customer;
import com.crm.springmvc.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    // ── GET /customer/list ────────────────────────────────────────────────────
    @GetMapping("/list")
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getCustomers();
        model.addAttribute("customers", customers);
        logger.info("Listing {} customers", customers.size());
        return "list-customers";
    }

    // ── GET /customer/showFormForAdd ──────────────────────────────────────────
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }

    // ── POST /customer/saveCustomer ───────────────────────────────────────────
    @PostMapping("/saveCustomer")
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "customer-form";
        }
        customerService.saveCustomer(customer);

        String action = (customer.getId() == 0) ? "added" : "updated";
        redirectAttributes.addFlashAttribute("successMessage",
                "Customer '" + customer.getFullName() + "' was successfully " + action + ".");
        logger.info("Saved customer: {}", customer);
        return "redirect:/customer/list";
    }

    // ── GET /customer/showFormForUpdate?customerId={id} ───────────────────────
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("customerId") int customerId, Model model) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            logger.warn("Customer with id {} not found for update", customerId);
            return "redirect:/customer/list";
        }
        model.addAttribute("customer", customer);
        return "customer-form";
    }

    // ── GET /customer/delete?customerId={id} ──────────────────────────────────
    @GetMapping("/delete")
    public String deleteCustomer(@RequestParam("customerId") int customerId,
                                 RedirectAttributes redirectAttributes) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer != null) {
            customerService.deleteCustomer(customerId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Customer '" + customer.getFullName() + "' was successfully deleted.");
            logger.info("Deleted customer id={}", customerId);
        }
        return "redirect:/customer/list";
    }

    // ── GET /customer/search?searchName={query} ───────────────────────────────
    @GetMapping("/search")
    public String searchCustomers(@RequestParam(value = "searchName", required = false) String searchName,
                                   Model model) {
        List<Customer> customers = customerService.searchCustomers(searchName);
        model.addAttribute("customers", customers);
        model.addAttribute("searchName", searchName);
        logger.info("Search '{}' returned {} results", searchName, customers.size());
        return "list-customers";
    }
}
