DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    role varchar(255) DEFAULT NULL,
    CONSTRAINT uk_g50w4r0ru3g9uf6i6fr4kpro8 UNIQUE (role)
);

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  first_name varchar(50) DEFAULT NULL,
  last_name varchar(50) DEFAULT NULL,
  date_of_birth date NOT NULL,
  username varchar(50) NOT NULL,
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  activation varchar(255) DEFAULT NULL,
  enabled BOOLEAN NOT NULL,
  status varchar(1) NOT NULL,
  CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
  CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)
);

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles (
  user_id BIGSERIAL NOT NULL,
  role_id BIGSERIAL NOT NULL,
  CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES roles(id)
);

SELECT setval('users_id_seq', MAX(id)) FROM users;
