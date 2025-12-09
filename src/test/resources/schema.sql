-- Note: This schema.sql is not loaded when using spring.jpa.hibernate.ddl-auto=create-drop
-- Hibernate generates the schema from entity classes automatically.
-- This file is kept for documentation and reference only.

DROP TABLE IF EXISTS image;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(2000),
    execution_date DATE,
    work_type VARCHAR(255),
    customer_type VARCHAR(255),
    creation_date DATE
);

CREATE TABLE image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(500),
    image_type VARCHAR(255),
    is_featured BOOLEAN,
    project_id BIGINT,
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES project(id)
);
