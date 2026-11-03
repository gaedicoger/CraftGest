INSERT INTO users (pseudo, mail, password_hash, business, slug, role, status, mail_verified)
VALUES
    ('Sylvie', 'sylvie@test.fr', '$2a$10$ZLCm.q9IrmIwcDYjfWzhVebXd2L/m1kMNBlew9.AjViaXZFu.HBRm', 'Sylfanch Créations', 'sylfanchcreations', 'ARTISAN', 'ACTIVE',true),
    ('Jacqueline', 'familleprudence@test.fr', '$2a$10$4SfbYE8IAs05ktDe8yxEUOSW8eOpU9JRBykhwa3B1npYuf.hUsStq', 'Pomme d''API', 'pommedapi', 'ARTISAN', 'ACTIVE', true),
    ('Coeurdefer', 'coeurdefer@test.fr', '$2a$10$Y25lsjJyvltwIW2xcL5JFO38dpRvT1PiVZsoAlOSJ/tfQIvV2L3gq', 'Coeur de Fer', 'coeurdefer', 'ARTISAN', 'ACTIVE', true),
    ('AdminTest', 'admin@test.fr', '$2a$10$vziACxf8GoZ9GBeyNFrM0OJh0zsX2/L6zfrVEOX2tUwlOqX.L2CKe', 'CraftGest', 'craftgest','ADMIN', 'ACTIVE', true);

INSERT INTO collections (name, user_id)
VALUES
    ('Noël 2026', 1),
    ('Ete 2026', 1),
    ('Sardines', 1),
    ('Automne 2026', 2),
    ('Mer', 3),
    ('Linogravure', 3),
    ('Enfance', 3),
    ('Souvenirs', 3);

INSERT INTO colors (name, user_id)
VALUES
    ('rouge', 1),
    ('bleu', 1),
    ('vert', 1),
    ('jaune', 1),
    ('orange', 1),
    ('doré', 1),
    ('argent', 1),
    ('noir', 1),
    ('violet', 1),
    ('rouge', 2),
    ('bleu', 2),
    ('vert', 2),
    ('jaune', 2),
    ('orange', 2),
    ('doré', 2),
    ('argent', 2),
    ('noir', 2),
    ('violet', 2),
    ('rouge', 3),
    ('bleu', 3),
    ('vert', 3),
    ('jaune', 3),
    ('orange', 3),
    ('doré', 3),
    ('argent', 3),
    ('noir', 3),
    ('violet', 3);

INSERT INTO findings (name, user_id)
VALUES
    ('Clous dorés', 1),
    ('Clous argentés', 1),
    ('Pendants dorés', 1),
    ('Pendants argentés', 1),
    ('Boucles inox', 1);

INSERT INTO materials (name, user_id)
VALUES
    ('Plastique fou',1),
    ('Apprêts inox', 1),
    ('Polymère', 1),
    ('Papier', 1),
    ('Cuir', 1),
    ('Bois', 1);

INSERT INTO places (name, type, discount_rate, commission_rate, user_id)
VALUES
    ('Musée de Douarnenez', 'Musée', 10, 0,1),
    ('Salon Coiffure Ergue', 'Commerce', 0, 5, 1),
    ('Marché de Pont l''Abbé', 'Marché', 0, 0, 1 );

INSERT INTO types (name, user_id)
VALUES
    ('Boucles d''oreilles', 1),
    ('Bague', 1),
    ('Bracelet', 1),
    ('Collier', 1);

INSERT INTO universes (name, user_id)
VALUES
    ('Noël', 1),
    ('Plage', 1),
    ('Enfance', 1),
    ('Mer', 1),
    ('Bretagne', 1);

INSERT INTO products (name, code_ref, description, price, cost_material, size, stock, is_visible_site, archive, user_id, type_id, collection_id, finding_id)
VALUES
    ('Boucles d''oreilles sardines rouge', 'BO_SA_RG', 'Jolies petites sardines en pêchées en Finistère sud, motifs', 20, 10, '50 mm', 2, false,false,1,1,3,1),
    ('Boucles d''oreilles sardines bleues', 'BO_SA_BG', 'Jolies petites sardines en pêchées en Finistère sud, motifs', 20, 10, '50 mm', 2, false,false,1,1,3,1),
    ('Boucles d''oreilles sardines vertes', 'BO_SA_VR', 'Jolies petites sardines en pêchées en Finistère sud, motifs', 20, 10, '50 mm', 2, false,false,1,1,3,1),
    ('Boucles d''oreilles sardines violettes', 'BO_SA_VIO', 'Jolies petites sardines en pêchées en Finistère sud, motifs', 20, 10, '50 mm', 2, false,false,1,1,3,1),
    ('Boucles d''oreilles sardines jaunes', 'BO_SA_JN', 'Jolies petites sardines en pêchées en Finistère sud, motifs', 20, 10, '50 mm', 2, false,false,1,1,3,1);

INSERT INTO product_colors (product_id, color_id)
VALUES
    (1, 1),   -- sardines rouge    → rouge
    (2, 2),   -- sardines bleues   → bleu
    (3, 3),   -- sardines vertes   → vert
    (4, 9),   -- sardines violettes → violet
    (5, 4);   -- sardines jaunes   → jaune

INSERT INTO product_materials (product_id, material_id)
VALUES
    (1, 1), (1, 2),
    (2, 1), (2, 2),
    (3, 1), (3, 2),
    (4, 1), (4, 2),
    (5, 1), (5, 2);

INSERT INTO product_universes (product_id, universe_id)
VALUES
    (1, 4), (1, 5),
    (2, 4), (2, 5),
    (3, 4), (3, 5),
    (4, 4), (4, 5),
    (5, 4), (5, 5);

INSERT INTO events (name, type, date_beginning, date_end, status, fees, commission_rate, user_id, place_id)
VALUES
    ('Dépots juin 2026 Musée', 'Dépôt vente', '2026-06-10', NULL, 'IN_PROGRESS', NULL, 0,1,1),
    ('Dépôt mai 2026 Salon Exhaus''Tif', 'Dépôt vente', '2026-05-12', NULL, 'IN_PROGRESS',NULL,0, 1, 2  ),
    ('Marché de Pont l''Abbé Ete 2026', 'Marché', '2026-08-10', '2026-08-10', 'CLOSED', 50, 0,1,3);

INSERT INTO sales (sale_date, quantity, price_paid, price_catalog, paid_type, event_id, product_id)
VALUES
    ('2026-08-10', 1, 20, 20, 'CB', 3,1),
    ('2026-08-10', 1, 20, 20, 'Cash', 3,2),
    ('2026-08-10', 1, 20, 20, 'CB', 3,3),
    ('2026-08-10', 1, 20, 20, 'CB', 3,4),
    ('2026-08-10', 1, 15, 20, 'Cash', 3,5);

INSERT INTO deposits (event_id, product_id, quantity_deposited)
VALUES
    (1,1,5),
    (1,2,5),
    (1,3,5),
    (1,4,5),
    (1,5,5),
    (2,1,5),
    (2,2,5),
    (2,3,5);