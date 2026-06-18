package com.crm.springmvc.dao;

import com.crm.springmvc.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@RunWith(MockitoJUnitRunner.class)
public class CustomerDAOTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query customerQuery;

    @Mock
    private Query deleteQuery;

    @InjectMocks
    private CustomerDAOImpl customerDAO;

    private Customer testCustomer;

    @Before
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        testCustomer = new Customer("John", "Adams", "john@gmail.com");
        testCustomer.setId(1);
    }

    @Test
    public void testGetCustomers_returnsAll() {
        List<Customer> expected = Arrays.asList(testCustomer);
        when(session.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
        when(customerQuery.getResultList()).thenReturn(expected);

        List<Customer> result = customerDAO.getCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(session, times(1)).createQuery(anyString(), eq(Customer.class));
    }

    @Test
    public void testGetCustomer_existingId() {
        when(session.get(Customer.class, 1)).thenReturn(testCustomer);

        Customer result = customerDAO.getCustomer(1);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(session, times(1)).get(Customer.class, 1);
    }

    @Test
    public void testGetCustomer_nonExistingId_returnsNull() {
        when(session.get(Customer.class, 999)).thenReturn(null);

        Customer result = customerDAO.getCustomer(999);

        assertNull(result);
    }

    @Test
    public void testSaveCustomer_callsSaveOrUpdate() {
        doNothing().when(session).saveOrUpdate(testCustomer);

        customerDAO.saveCustomer(testCustomer);

        verify(session, times(1)).saveOrUpdate(testCustomer);
    }

    @Test
    public void testDeleteCustomer_executesDeleteQuery() {
        when(session.createQuery(anyString())).thenReturn(deleteQuery);
        when(deleteQuery.setParameter(anyString(), any())).thenReturn(deleteQuery);
        when(deleteQuery.executeUpdate()).thenReturn(1);

        customerDAO.deleteCustomer(1);

        verify(session, times(1)).createQuery(anyString());
        verify(deleteQuery, times(1)).setParameter(eq("customerId"), eq(1));
        verify(deleteQuery, times(1)).executeUpdate();
    }

    @Test
    public void testSearchCustomers_withBlankName_returnsAll() {
        List<Customer> expected = Arrays.asList(testCustomer);
        when(session.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
        when(customerQuery.getResultList()).thenReturn(expected);

        List<Customer> result = customerDAO.searchCustomers("");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testSearchCustomers_withValidName() {
        List<Customer> expected = Arrays.asList(testCustomer);
        when(session.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
        when(customerQuery.setParameter(anyString(), anyString())).thenReturn(customerQuery);
        when(customerQuery.getResultList()).thenReturn(expected);

        List<Customer> result = customerDAO.searchCustomers("john");

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
