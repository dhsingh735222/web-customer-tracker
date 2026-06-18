package com.crm.springmvc.service;

import com.crm.springmvc.dao.CustomerDAO;
import com.crm.springmvc.entity.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer1;
    private Customer customer2;

    @Before
    public void setUp() {
        customer1 = new Customer("John", "Adams", "john@gmail.com");
        customer1.setId(1);

        customer2 = new Customer("Chitvan", "Dixit", "chitvan.dixit@gmail.com");
        customer2.setId(2);
    }

    @Test
    public void testGetCustomers_returnsAllCustomers() {
        List<Customer> mockList = Arrays.asList(customer1, customer2);
        when(customerDAO.getCustomers()).thenReturn(mockList);

        List<Customer> result = customerService.getCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerDAO, times(1)).getCustomers();
    }

    @Test
    public void testGetCustomers_returnsEmptyList() {
        when(customerDAO.getCustomers()).thenReturn(Arrays.asList());

        List<Customer> result = customerService.getCustomers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCustomer_existingId_returnsCustomer() {
        when(customerDAO.getCustomer(1)).thenReturn(customer1);

        Customer result = customerService.getCustomer(1);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Adams", result.getLastName());
        verify(customerDAO, times(1)).getCustomer(1);
    }

    @Test
    public void testGetCustomer_nonExistingId_returnsNull() {
        when(customerDAO.getCustomer(999)).thenReturn(null);

        Customer result = customerService.getCustomer(999);

        assertNull(result);
    }

    @Test
    public void testSaveCustomer_newCustomer_callsDAO() {
        Customer newCustomer = new Customer("Donald", "Duck", "donald@gmail.com");
        doNothing().when(customerDAO).saveCustomer(newCustomer);

        customerService.saveCustomer(newCustomer);

        verify(customerDAO, times(1)).saveCustomer(newCustomer);
    }

    @Test
    public void testDeleteCustomer_callsDAODelete() {
        doNothing().when(customerDAO).deleteCustomer(1);

        customerService.deleteCustomer(1);

        verify(customerDAO, times(1)).deleteCustomer(1);
    }

    @Test
    public void testSearchCustomers_withValidName_returnsList() {
        when(customerDAO.searchCustomers("john")).thenReturn(Arrays.asList(customer1));

        List<Customer> result = customerService.searchCustomers("john");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(customerDAO, times(1)).searchCustomers("john");
    }

    @Test
    public void testSearchCustomers_withNull_returnsAll() {
        List<Customer> mockList = Arrays.asList(customer1, customer2);
        when(customerDAO.searchCustomers(null)).thenReturn(mockList);

        List<Customer> result = customerService.searchCustomers(null);

        assertEquals(2, result.size());
    }
}
