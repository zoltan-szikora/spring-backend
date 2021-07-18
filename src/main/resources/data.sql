INSERT INTO roles (id, role) VALUES (1,'ROLE_USER');
INSERT INTO roles (id, role) VALUES (2,'ROLE_ADMIN');

INSERT INTO users (id, first_name, last_name, username, date_of_birth, email, password, enabled, status, activation) VALUES
(1, 'Alfonz', 'Admin', 'admin', '1980-01-01', 'admin@test.com', '$2y$12$N9c94hKYwIAaq4jr9ZiPTuNfuE4bh0venCNLNmCoE6HAzIjIx79U6', TRUE, 'A', LOCALTIMESTAMP);

INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1),
(1, 2);