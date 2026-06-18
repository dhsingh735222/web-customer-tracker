# Database Guide

---

## Schema

```sql
CREATE TABLE customer (
    id         INT          NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_customer_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### Column Details

| Column | Type | Nullable | Notes |
|---|---|---|---|
| `id` | INT AUTO_INCREMENT | No | Primary key, surrogate |
| `first_name` | VARCHAR(50) | No | Customer's first name |
| `last_name` | VARCHAR(50) | No | Customer's last name |
| `email` | VARCHAR(100) | No | Unique per customer |

---

## Setup Commands

```bash
# Run the full setup script
mysql -u root -p < sql/setup.sql

# Or manually:
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS web_customer_tracker CHARACTER SET utf8mb4;"
mysql -u root -p -e "CREATE USER IF NOT EXISTS 'springstudent'@'localhost' IDENTIFIED BY 'springstudent';"
mysql -u root -p -e "GRANT ALL ON web_customer_tracker.* TO 'springstudent'@'localhost';"
```

---

## Sample Data

```sql
INSERT INTO customer (first_name, last_name, email) VALUES
    ('John',    'Adams',   'john@gmail.com'),
    ('Chitvan', 'Dixit',   'chitvan.dixit@gmail.com'),
    ('Donald',  'Duck',    'donald@gmail.com'),
    ('Ajay',    'Rao',     'ajay@gmail.com'),
    ('Shourya', 'Roy',     'Shourya.Roy@gmail.com');
```

---

## Hibernate HQL Queries

All queries are in `CustomerDAOImpl.java`:

### List All
```hql
FROM Customer ORDER BY lastName, firstName
```

### Find by ID
```java
session.get(Customer.class, id)
```

### Insert / Update
```java
session.saveOrUpdate(customer)
```
Hibernate determines insert vs update based on whether `id == 0`.

### Delete
```hql
DELETE FROM Customer WHERE id = :customerId
```

### Search
```hql
FROM Customer
WHERE lower(firstName) LIKE :name
   OR lower(lastName)  LIKE :name
   OR lower(email)     LIKE :name
ORDER BY lastName, firstName
```

---

## Hibernate DDL Auto

Controlled by `hibernate.hbm2ddl.auto` in `persistence.properties`:

| Value | Behaviour | Use when |
|---|---|---|
| `create` | Drop + recreate tables on every start | Fresh dev environment |
| `create-drop` | Create on start, drop on shutdown | Integration tests |
| `update` | Add missing columns/tables | Active development |
| `validate` | Verify schema matches entities, fail if not | Staging / production |
| `none` | Do nothing | Production with manual migrations |

**Recommended:** Use `update` during development, `validate` or `none` in production.

---

## Useful Admin Queries

```sql
-- Count all customers
SELECT COUNT(*) FROM customer;

-- Search by partial name
SELECT * FROM customer
WHERE LOWER(first_name) LIKE '%john%'
   OR LOWER(last_name)  LIKE '%john%';

-- Check for duplicate emails
SELECT email, COUNT(*) AS cnt
FROM customer
GROUP BY email
HAVING cnt > 1;

-- Reset auto-increment
ALTER TABLE customer AUTO_INCREMENT = 1;

-- Truncate (delete all rows)
TRUNCATE TABLE customer;
```

---

## Backup & Restore

```bash
# Backup
mysqldump -u springstudent -pspringstudent web_customer_tracker > backup.sql

# Restore
mysql -u springstudent -pspringstudent web_customer_tracker < backup.sql
```
