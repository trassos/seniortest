-- Migration para inserir registros na tabela order_items e orders
INSERT INTO orders (id, value, discount, total, status)
VALUES ('5448f2a2-9ac0-4e81-8131-86401e3bc824', 647.00, 21.56, 625.44, 'PENDING');

INSERT INTO order_item (id, order_id, product_id, item_quantity, discount, total)
VALUES
    ('73a38d00-a900-4654-85e6-f04af47ff831', '5448f2a2-9ac0-4e81-8131-86401e3bc824', '6fce2e1b-f54b-4416-9b1e-0b083f80efd0', 3.00, 0.00, 300.00),
    ('9b1543b0-b42c-453b-a77e-a5708524ef8b', '5448f2a2-9ac0-4e81-8131-86401e3bc824', '92415a5a-beb6-41c4-82d2-0d8a0d6fa1df', 33.00, 11.52, 120.48),
    ('df054afc-5f3c-4092-86d7-b075e70852c9', '5448f2a2-9ac0-4e81-8131-86401e3bc824', '2ed297ec-d0af-4671-9847-dd01183f27fd', 1.00, 10.04, 104.96),
    ('2347ad3f-2cef-4838-a4fe-aa6a06c9fa8b', '5448f2a2-9ac0-4e81-8131-86401e3bc824', '727d6944-4272-42cc-904c-6af01f35d96c', 2.00, 0.00, 100.00);
