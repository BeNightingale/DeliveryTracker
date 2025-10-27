USE delivery_tracking;
CREATE TABLE deliveries
(
    delivery_id                 INT NOT NULL AUTO_INCREMENT,
    delivery_number             VARCHAR(30) NOT NULL,
    delivery_created            DATETIME,
    delivery_status             VARCHAR(350) NOT NULL,
--   opis krótki polski w InPoSt
    status_description          VARCHAR(3000),
    status_change_datetime      DATETIME,
    deliverer                   VARCHAR(100) NOT NULL,
    delivery_description        VARCHAR(2000),
    finished                    BOOLEAN NOT NULL,
    PRIMARY KEY (delivery_id)
);

CREATE TABLE history
(
    id                          INT NOT NULL AUTO_INCREMENT,
    delivery_id                 INT NOT NULL,
    delivery_number             VARCHAR(30) NOT NULL,
    delivery_status             VARCHAR(350) NOT NULL,
    status_description          VARCHAR(3000),
    status_change_datetime      DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE users
(
    user_id                     INT NOT NULL AUTO_INCREMENT,
    user_name                   VARCHAR(50) NOT NULL UNIQUE,
    email                       VARCHAR(150) NOT NULL UNIQUE,
    password                    VARCHAR(100) NOT NULL,
    enabled                     BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE permissions
(
    permission_id               INT NOT NULL AUTO_INCREMENT,
    user_name                   VARCHAR(50) NOT NULL,
    permission                  VARCHAR(50) NOT NULL,
    PRIMARY KEY (permission_id),
    FOREIGN KEY (user_name) REFERENCES users(user_name)
);

CREATE TABLE pending_users
(
    pending_user_id             INT NOT NULL AUTO_INCREMENT,
    user_name                   VARCHAR(50) NOT NULL UNIQUE,
    email                       VARCHAR(150) NOT NULL UNIQUE,
    password                    VARCHAR(100) NOT NULL,
    token                       VARCHAR(36) NOT NULL UNIQUE,
    token_expiration_date       DATETIME NOT NULL,
    creation_date               DATETIME DEFAULT CURRENT_TIMESTAMP,
    confirmed                   BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (pending_user_id)
);

INSERT INTO deliveries(delivery_id, delivery_number, delivery_created, delivery_status, status_change_datetime, deliverer, delivery_description, finished)
VALUES ('1', '12345678', '2023-09-10T10:14:10', 'DELIVERED', '2023-10-12T12:15:01','INPOST','radio', true);
INSERT INTO deliveries(delivery_id, delivery_number, delivery_created, delivery_status, status_change_datetime, deliverer, finished)
VALUES ('2', '660166696359300112430272', '2023-08-02T08:10:10', 'ON_THE_ROAD', '2023-08-24T11:15:19', 'INPOST', false);
INSERT INTO deliveries(delivery_id, delivery_number, delivery_created, delivery_status, deliverer, delivery_description, finished)
VALUES ('3', '605552596359300024984977', '2023-11-10T12:14:10', 'CONFIRMED', 'INPOST', 'jacket', false);
INSERT INTO deliveries(delivery_id, delivery_number, delivery_created, delivery_status, deliverer, delivery_description, finished)
VALUES ('4', '660166976359300115325076', '2023-07-10T12:14:10', 'NOT_FOUND', 'INPOST', 'pen', false);

INSERT INTO history(id, delivery_id, delivery_number, delivery_status, status_description, status_change_datetime)
VALUES ('1', '1', '12345678', 'DELIVERED', null, '2023-10-12 12:15:01');
INSERT INTO history(id, delivery_id, delivery_number, delivery_status, status_description, status_change_datetime)
VALUES ('2', '2', '660166696359300112430272', 'UNKNOWN', null, '2023-09-10 16:15:01');
INSERT INTO history(id, delivery_id, delivery_number, delivery_status, status_description, status_change_datetime)
VALUES ('3', '2', '660166696359300112430272', 'CONFIRMED', null, '2023-09-11 10:10:01');

INSERT INTO users(user_id, user_name, email, password, enabled)
VALUES('1', 'myUser', 'user@gmail.com', '$2a$10$8xAiFhrrLbTxidvDWKvQ6eX9PlPspTkWqV0JHFQlf0lFZAvfwXbHe', true);

INSERT INTO users(user_id, user_name, email, password, enabled)
VALUES('2', 'admin', 'admin@gmail.com', '$2a$10$ddgM6X3plEIz/FN08wE7WOMhSvxd0m9xEpC30gGV5uGlkFFSOA5v6', true);

INSERT INTO permissions(permission_id, user_name, permission)
VALUES('1', 'myUser', 'ROLE_USER');

INSERT INTO permissions(permission_id, user_name, permission)
VALUES('2', 'admin', 'ROLE_ADMIN');