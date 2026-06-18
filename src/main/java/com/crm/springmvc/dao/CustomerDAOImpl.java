package com.crm.springmvc.dao;

import com.crm.springmvc.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> getCustomers() {
        Session session = sessionFactory.getCurrentSession();
        Query<Customer> query = session.createQuery(
                "FROM Customer ORDER BY lastName, firstName", Customer.class);
        List<Customer> customers = query.getResultList();
        logger.debug("Retrieved {} customers", customers.size());
        return customers;
    }

    @Override
    public void saveCustomer(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(customer);
        logger.debug("Saved customer: {}", customer);
    }

    @Override
    public Customer getCustomer(int id) {
        Session session = sessionFactory.getCurrentSession();
        Customer customer = session.get(Customer.class, id);
        logger.debug("Fetched customer by id {}: {}", id, customer);
        return customer;
    }

    @Override
    public void deleteCustomer(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query<?> query = session.createQuery("DELETE FROM Customer WHERE id = :customerId");
        query.setParameter("customerId", id);
        int rowsDeleted = query.executeUpdate();
        logger.debug("Deleted {} customer(s) with id {}", rowsDeleted, id);
    }

    @Override
    public List<Customer> searchCustomers(String searchName) {
        Session session = sessionFactory.getCurrentSession();
        Query<Customer> query;

        if (searchName != null && !searchName.trim().isEmpty()) {
            String like = "%" + searchName.toLowerCase() + "%";
            query = session.createQuery(
                    "FROM Customer WHERE lower(firstName) LIKE :name" +
                    " OR lower(lastName) LIKE :name" +
                    " OR lower(email) LIKE :name" +
                    " ORDER BY lastName, firstName",
                    Customer.class);
            query.setParameter("name", like);
        } else {
            query = session.createQuery(
                    "FROM Customer ORDER BY lastName, firstName", Customer.class);
        }

        List<Customer> results = query.getResultList();
        logger.debug("Search '{}' returned {} results", searchName, results.size());
        return results;
    }
}
