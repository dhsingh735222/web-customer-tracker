-- ============================================================
-- CRM - Customer Relationship Manager
-- Database Setup Script
-- Run this script once to prepare your MySQL database
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS web_customer_tracker
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE web_customer_tracker;

-- Step 2: Create a dedicated database user
--   (skip if you prefer to use root for local dev)
CREATE USER IF NOT EXISTS 'springstudent'@'localhost'
    IDENTIFIED BY 'springstudent';

GRANT ALL PRIVILEGES ON web_customer_tracker.*
    TO 'springstudent'@'localhost';

FLUSH PRIVILEGES;

-- Step 3: Create the customer table
CREATE TABLE IF NOT EXISTS customer (
    id         INT          NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_customer_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Step 4: Seed sample data (matching the screenshots)
INSERT INTO customer (first_name, last_name, email) VALUES
    ('John',    'Adams',   'john@gmail.com'),
    ('Chitvan', 'Dixit',   'chitvan.dixit@gmail.com'),
    ('Donald',  'Duck',    'donald@gmail.com'),
    ('Ajay',    'Rao',     'ajay@gmail.com'),
    ('Shourya', 'Roy',     'Shourya.Roy@gmail.com');

-- Verify
SELECT * FROM customer;
