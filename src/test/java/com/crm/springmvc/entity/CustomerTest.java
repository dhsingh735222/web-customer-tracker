package com.crm.springmvc.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void testDefaultConstructor() {
        Customer customer = new Customer();
        assertEquals(0, customer.getId());
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getEmail());
    }

    @Test
    public void testParameterizedConstructor() {
        Customer customer = new Customer("John", "Adams", "john@gmail.com");
        assertEquals("John", customer.getFirstName());
        assertEquals("Adams", customer.getLastName());
        assertEquals("john@gmail.com", customer.getEmail());
    }

    @Test
    public void testGettersAndSetters() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setEmail("jane@example.com");

        assertEquals(1, customer.getId());
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("jane@example.com", customer.getEmail());
    }

    @Test
    public void testGetFullName() {
        Customer customer = new Customer("John", "Adams", "john@gmail.com");
        assertEquals("John Adams", customer.getFullName());
    }

    @Test
    public void testToString() {
        Customer customer = new Customer("John", "Adams", "john@gmail.com");
        customer.setId(1);
        String result = customer.toString();
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Adams"));
        assertTrue(result.contains("john@gmail.com"));
    }
}
