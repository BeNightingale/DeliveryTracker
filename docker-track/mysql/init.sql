USE delivery_tracking;
CREATE TABLE deliveries
(
    delivery_id                 INT NOT NULL AUTO_INCREMENT,
    delivery_number             VARCHAR(30) NOT NULL,
    delivery_created            DATETIME,
    delivery_status             VARCHAR(350) NOT NULL,
    status_description          VARCHAR(3000), ### krótki polski w InPoSt
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