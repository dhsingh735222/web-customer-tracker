# Architecture Guide

---

## MVC Architecture Overview

```
Browser (HTTP Request)
        │
        ▼
┌───────────────────────────────────────────────────────┐
│                  Apache Tomcat 9                       │
│  ┌────────────────────────────────────────────────┐   │
│  │          Spring DispatcherServlet               │   │
│  │              (Front Controller)                 │   │
│  └────────────────────┬───────────────────────────┘   │
│                       │ routes to                     │
│  ┌────────────────────▼───────────────────────────┐   │
│  │           CustomerController                    │   │
│  │  @GetMapping  @PostMapping  @RequestParam       │   │
│  └────────────────────┬───────────────────────────┘   │
│                       │ calls                         │
│  ┌────────────────────▼───────────────────────────┐   │
│  │           CustomerService (interface)           │   │
│  │           CustomerServiceImpl                   │   │
│  │           @Transactional                        │   │
│  └────────────────────┬───────────────────────────┘   │
│                       │ calls                         │
│  ┌────────────────────▼───────────────────────────┐   │
│  │           CustomerDAO (interface)               │   │
│  │           CustomerDAOImpl                       │   │
│  │           Hibernate SessionFactory              │   │
│  └────────────────────┬───────────────────────────┘   │
│                       │ SQL                           │
└───────────────────────┼───────────────────────────────┘
                        │
               ┌────────▼────────┐
               │   MySQL 8.0     │
               │  customer table  │
               └─────────────────┘
```

---

## Layer Responsibilities

### Controller Layer (`com.crm.springmvc.controller`)
- Receives HTTP requests from the browser
- Reads request parameters (`@RequestParam`, `@ModelAttribute`)
- Calls the Service layer
- Sets model attributes for the view
- Returns a view name or redirect instruction

### Service Layer (`com.crm.springmvc.service`)
- Contains all business logic
- Manages transaction boundaries with `@Transactional`
- Acts as the boundary between Controller and DAO
- Service interface keeps Controller decoupled from DAO implementation

### DAO Layer (`com.crm.springmvc.dao`)
- Direct Hibernate SessionFactory usage
- All HQL queries live here
- No business logic — pure data access
- Implements CRUD operations + search

### Entity Layer (`com.crm.springmvc.entity`)
- POJO class annotated with JPA annotations
- Maps to the `customer` table via `@Entity` + `@Table`
- Bean Validation annotations for input validation

---

## Request Lifecycle — List Customers

```
1. Browser  GET /customer/list
2. Tomcat → DispatcherServlet
3. DispatcherServlet → CustomerController.listCustomers(Model)
4. Controller → CustomerServiceImpl.getCustomers()
5. Service (in @Transactional) → CustomerDAOImpl.getCustomers()
6. DAO opens Hibernate Session → executes HQL "FROM Customer ORDER BY lastName"
7. Hibernate → MySQL SELECT * FROM customer ORDER BY last_name
8. ResultSet → List<Customer> objects
9. DAO returns List → Service returns List → Controller adds to Model
10. Controller returns "list-customers"
11. DispatcherServlet → InternalResourceViewResolver → /WEB-INF/jsp/list-customers.jsp
12. JSP rendered with JSTL → HTML response → Browser
```

---

## Spring Configuration

### Bean Wiring
All beans are discovered via `<context:component-scan base-package="com.crm.springmvc" />` in `spring-mvc-crud-demo-servlet.xml`.

| Annotation | Bean | Purpose |
|---|---|---|
| `@Controller` | CustomerController | Spring MVC controller |
| `@Service` | CustomerServiceImpl | Business logic |
| `@Repository` | CustomerDAOImpl | Data access |
| `@Entity` | Customer | JPA entity |

### Transaction Management
- `HibernateTransactionManager` wired to the `SessionFactory`
- `<tx:annotation-driven />` enables `@Transactional` processing
- All service methods are `@Transactional`
- Read-only operations use `@Transactional(readOnly = true)` for performance

### Connection Pool (C3P0)
```
minPoolSize=5  maxPoolSize=20  maxIdleTime=30000ms
```

---

## Database Design

```sql
CREATE TABLE customer (
    id         INT          NOT NULL AUTO_INCREMENT,  -- PK, surrogate key
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_customer_email (email)
);
```

---

## Package Structure

```
com.crm.springmvc
├── controller
│   └── CustomerController.java       ← @Controller
├── dao
│   ├── CustomerDAO.java              ← Interface
│   └── CustomerDAOImpl.java          ← @Repository
├── entity
│   └── Customer.java                 ← @Entity
└── service
    ├── CustomerService.java          ← Interface
    └── CustomerServiceImpl.java      ← @Service
```

---

## Security Considerations

- All user input is validated server-side via Bean Validation (`@NotBlank`, `@Email`)
- Spring MVC's `<form:form>` tag automatically generates CSRF-safe form bindings
- Email uniqueness is enforced at the database level via a UNIQUE constraint
- JSP output uses JSTL `<c:out>` (implicitly escaped) to prevent XSS
- Parameterized HQL queries prevent SQL injection
