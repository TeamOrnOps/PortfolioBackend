-- Insert test users (matches User entity: users table with username, email, password columns)
INSERT INTO users(username, email, password) VALUES
    ('admin', 'admin@example.com', 'testpassword'),
    ('testuser', 'test@example.com', 'testpassword');

-- Insert test projects (matches Project entity: work_type instead of service_category)
INSERT INTO project(title, description, execution_date, work_type, customer_type, creation_date) VALUES
     ('Fliserens', 'Rensning af terrasse', '2025-03-04', 'PAVING_CLEANING', 'PRIVATE_CUSTOMER', '2025-01-12'),
     ('Tagrens', 'Algebehandling af tag', '2025-02-10', 'ROOF_CLEANING', 'BUSINESS_CUSTOMER', '2025-01-12');

-- Insert test images (matches Image entity)
INSERT INTO image(url, image_type, is_featured, project_id) VALUES
    ('https://example.com/before1.jpg', 'BEFORE', false, 1),
    ('https://example.com/after1.jpg', 'AFTER', true, 1),
    ('https://example.com/before2.jpg', 'BEFORE', false, 2),
    ('https://example.com/after2.jpg', 'AFTER', true, 2);
