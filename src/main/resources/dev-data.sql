-- ============================================
-- AlgeNord Dev Data
-- Test bruger og projekter med før/efter billeder
-- ============================================

-- Test bruger (email: dev@example.com, password: devpassword)
INSERT INTO users(email, password, username) VALUES
    ('dev@example.com','$2a$12$7hpQEehzB4SDFKpoqBZlm.PFsjYqBDIIR2j.pm/MmNav9wFRMeSdq', 'dev');

-- ============================================
-- PROJEKTER
-- ============================================

-- Projekt 1: Belægningsrensning (PAVING_CLEANING) - Private
INSERT INTO project(title, description, execution_date, work_type, customer_type, creation_date)
VALUES ('Belægningsrensning på privat indkørsel',
        'Professionel rensning af flisebelægning med højtryksrenser. Fjernelse af mos, alger og snavs fra indkørsel og terrasse.',
        '2024-12-05', 'PAVING_CLEANING', 'PRIVATE_CUSTOMER', '2024-12-01');

-- Projekt 2: Facaderensning (FACADE_CLEANING) - Business
INSERT INTO project(title, description, execution_date, work_type, customer_type, creation_date)
VALUES ('Facaderensning af kontorbygning',
        'Omfattende rensning af bygningsfacade. Fjernelse af grønne alger, forurening og vejrsnavs fra murværk.',
        '2024-11-20', 'FACADE_CLEANING', 'BUSINESS_CUSTOMER', '2024-11-15');

-- Projekt 3: Fliserensning (PAVING_CLEANING) - Business
INSERT INTO project(title, description, execution_date, work_type, customer_type, creation_date)
VALUES ('Fjernelse af mos og alger fra butiksfacade',
        'Grundig fjernelse af grøn belægning fra flisebelagt forplads. Behandling mod fremtidigt genvækst.',
        '2024-10-15', 'PAVING_CLEANING', 'BUSINESS_CUSTOMER', '2024-10-10');

-- Projekt 4: Tagrensning (ROOF_CLEANING) - Business
INSERT INTO project(title, description, execution_date, work_type, customer_type, creation_date)
VALUES ('Tagrensning af erhvervsejendom',
        'Professionel rensning af tagflader. Fjernelse af mos, lav og alger fra tagsten for at forlænge tagets levetid.',
        '2024-09-25', 'ROOF_CLEANING', 'BUSINESS_CUSTOMER', '2024-09-20');

-- Projekt 5: Træterrasse rensning (WOODEN_DECK_CLEANING) - Private
INSERT INTO project(title, description, execution_date, work_type, customer_type, creation_date)
VALUES ('Træterrasse behandling og rensning',
        'Skånsom rensning af træterrasse med specialudstyr. Fjernelse af grønsvær og fornyelse af træets naturlige farve.',
        '2024-08-10', 'WOODEN_DECK_CLEANING', 'PRIVATE_CUSTOMER', '2024-08-05');

-- ============================================
-- BILLEDER
-- ============================================

-- Projekt 1 billeder (PAVING_CLEANING)
INSERT INTO image(url, image_type, is_featured, project_id) VALUES
    ('http://localhost:8080/uploads/IMG_0116.png', 'BEFORE', false, 1),
    ('http://localhost:8080/uploads/IMG_0126.png', 'AFTER', false, 1),
    ('http://localhost:8080/uploads/IMG_0126.png', null, true, 1);

-- Projekt 2 billeder (FACADE_CLEANING)
INSERT INTO image(url, image_type, is_featured, project_id) VALUES
    ('http://localhost:8080/uploads/IMG_0130.png', 'BEFORE', false, 2),
    ('http://localhost:8080/uploads/IMG_0135-1.png', 'AFTER', false, 2),
    ('http://localhost:8080/uploads/IMG_0135-1.png', null, true, 2);

-- Projekt 3 billeder (PAVING_CLEANING - Business)
INSERT INTO image(url, image_type, is_featured, project_id) VALUES
    ('http://localhost:8080/uploads/IMG_1420.png', 'BEFORE', false, 3),
    ('http://localhost:8080/uploads/IMG_1432-1.png', 'AFTER', false, 3),
    ('http://localhost:8080/uploads/IMG_1432-1.png', null, true, 3);

-- Projekt 4 billeder (ROOF_CLEANING)
INSERT INTO image(url, image_type, is_featured, project_id) VALUES
    ('http://localhost:8080/uploads/IMG_3561-scaled_3.png', 'BEFORE', false, 4),
    ('http://localhost:8080/uploads/IMG_3562-scaled_3.png', 'AFTER', false, 4),
    ('http://localhost:8080/uploads/IMG_3562-scaled_3.png', null, true, 4);

-- Projekt 5 billeder (WOODEN_DECK_CLEANING)
INSERT INTO image(url, image_type, is_featured, project_id) VALUES
    ('http://localhost:8080/uploads/IMG_0116.png', 'BEFORE', false, 5),
    ('http://localhost:8080/uploads/IMG_0130.png', 'AFTER', false, 5),
    ('http://localhost:8080/uploads/IMG_0130.png', null, true, 5);