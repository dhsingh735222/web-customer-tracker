-- ============================================================
-- Docker container database initialisation
-- This file is auto-executed by the MySQL Docker image
-- on first container start (via /docker-entrypoint-initdb.d)
-- ============================================================

CREATE DATABASE IF NOT EXISTS web_customer_tracker
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE web_customer_tracker;

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

INSERT INTO customer (first_name, last_name, email) VALUES
    ('John',    'Adams',   'john@gmail.com'),
    ('Chitvan', 'Dixit',   'chitvan.dixit@gmail.com'),
    ('Donald',  'Duck',    'donald@gmail.com'),
    ('Ajay',    'Rao',     'ajay@gmail.com'),
    ('Shourya', 'Roy',     'Shourya.Roy@gmail.com');
