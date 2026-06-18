package com.crm.springmvc.controller;

import com.crm.springmvc.entity.Customer;
import com.crm.springmvc.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer1;
    private Customer customer2;
    private Model model;
    private RedirectAttributes redirectAttributes;

    @Before
    public void setUp() {
        customer1 = new Customer("John", "Adams", "john@gmail.com");
        customer1.setId(1);

        customer2 = new Customer("Chitvan", "Dixit", "chitvan.dixit@gmail.com");
        customer2.setId(2);

        model = new ExtendedModelMap();
        redirectAttributes = new RedirectAttributesModelMap();
    }

    // ── listCustomers ─────────────────────────────────────────────────────────

    @Test
    public void testListCustomers_returnsListView() {
        List<Customer> mockList = Arrays.asList(customer1, customer2);
        when(customerService.getCustomers()).thenReturn(mockList);

        String viewName = customerController.listCustomers(model);

        assertEquals("list-customers", viewName);
        assertEquals(mockList, model.asMap().get("customers"));
        verify(customerService, times(1)).getCustomers();
    }

    // ── showFormForAdd ────────────────────────────────────────────────────────

    @Test
    public void testShowFormForAdd_returnsFormView() {
        String viewName = customerController.showFormForAdd(model);

        assertEquals("customer-form", viewName);
        assertNotNull(model.asMap().get("customer"));
        assertTrue(model.asMap().get("customer") instanceof Customer);
    }

    // ── saveCustomer ──────────────────────────────────────────────────────────

    @Test
    public void testSaveCustomer_validData_redirectsToList() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = customerController.saveCustomer(customer1, bindingResult, redirectAttributes);

        assertEquals("redirect:/customer/list", viewName);
        verify(customerService, times(1)).saveCustomer(customer1);
    }

    @Test
    public void testSaveCustomer_validationErrors_returnsForm() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = customerController.saveCustomer(customer1, bindingResult, redirectAttributes);

        assertEquals("customer-form", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    // ── showFormForUpdate ─────────────────────────────────────────────────────

    @Test
    public void testShowFormForUpdate_existingCustomer_returnsForm() {
        when(customerService.getCustomer(1)).thenReturn(customer1);

        String viewName = customerController.showFormForUpdate(1, model);

        assertEquals("customer-form", viewName);
        assertEquals(customer1, model.asMap().get("customer"));
    }

    @Test
    public void testShowFormForUpdate_notFound_redirectsToList() {
        when(customerService.getCustomer(999)).thenReturn(null);

        String viewName = customerController.showFormForUpdate(999, model);

        assertEquals("redirect:/customer/list", viewName);
    }

    // ── deleteCustomer ────────────────────────────────────────────────────────

    @Test
    public void testDeleteCustomer_existingCustomer_redirectsToList() {
        when(customerService.getCustomer(1)).thenReturn(customer1);

        String viewName = customerController.deleteCustomer(1, redirectAttributes);

        assertEquals("redirect:/customer/list", viewName);
        verify(customerService, times(1)).deleteCustomer(1);
    }

    @Test
    public void testDeleteCustomer_notFound_redirectsToList() {
        when(customerService.getCustomer(999)).thenReturn(null);

        String viewName = customerController.deleteCustomer(999, redirectAttributes);

        assertEquals("redirect:/customer/list", viewName);
        verify(customerService, never()).deleteCustomer(anyInt());
    }

    // ── searchCustomers ───────────────────────────────────────────────────────

    @Test
    public void testSearchCustomers_withSearchTerm_returnsFilteredList() {
        List<Customer> searchResults = Arrays.asList(customer1);
        when(customerService.searchCustomers("john")).thenReturn(searchResults);

        String viewName = customerController.searchCustomers("john", model);

        assertEquals("list-customers", viewName);
        assertEquals(searchResults, model.asMap().get("customers"));
        assertEquals("john", model.asMap().get("searchName"));
    }
}
