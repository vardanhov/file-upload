
INSERT INTO upload.access_user_list
VALUES (1, 2, DEFAULT, DEFAULT, DEFAULT, 'f', 'ROLE_ADMIN', 'lad'),
       (2, 3, DEFAULT, DEFAULT, DEFAULT, 'f', 'ROLE_ADMIN', 'lad'),
       (3, 4, DEFAULT, DEFAULT, DEFAULT, 'f', 'ROLE_USER', 'lad'),
       (4, 5, DEFAULT, '2020-03-14 16:26:33', '2021-08-16 16:26:33', 'f',
        'ROLE_VIEWER', 'lad'),
       (5, 6, DEFAULT, '2021-04-14 16:26:33', '2021-08-16 16:26:33', 'f',
        'ROLE_USER', 'lad'),
       (6, 7, DEFAULT, DEFAULT, DEFAULT, 'f', 'ROLE_USER', 'lad');


INSERT INTO upload.user_groups
VALUES (1, 'user'),
       (2, 'nesw'),
       (3, 'dfdds');

INSERT INTO upload.participant
VALUES (1, 'anton', 'user', 1),
       (2, 'admin', 'Администратор', 1),
       (3, 'vtb213684', 'Анвар', 1),
       (4, 'Sergey', 'Сергей Антонович Дрожжин', 1),
       (5, 'oleg', 'Олег Андреевич Петров', 1),
       (6, 'Andrey', 'Андрей Викторович Родионов', 1),
       (7, 'Stas', 'Стас Валентинович Герман ', 1);


