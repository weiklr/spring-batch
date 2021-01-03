-- DROP TABLE IF EXISTS billionaires;
--
-- CREATE TABLE billionaires (
--                               id INT AUTO_INCREMENT  PRIMARY KEY,
--                               first_name VARCHAR(250) NOT NULL,
--                               last_name VARCHAR(250) NOT NULL,
--                               career VARCHAR(250) DEFAULT NULL
-- );
--
-- INSERT INTO billionaires (first_name, last_name, career) VALUES
-- ('Aliko', 'Dangote', 'Billionaire Industrialist'),
-- ('Bill', 'Gates', 'Billionaire Tech Entrepreneur'),
-- ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate');


DROP TABLE people IF EXISTS;

DROP TABLE backup IF EXISTS;

CREATE TABLE people  (
                         person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
                         first_name VARCHAR(20),
                         last_name VARCHAR(20)
);

CREATE TABLE backup  (
                         person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
                         first_name VARCHAR(20),
                         last_name VARCHAR(20)
);