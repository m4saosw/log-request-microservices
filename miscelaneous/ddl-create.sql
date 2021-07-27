CREATE DATABASE "logrequestdb"

DROP TABLE log_request IF EXISTS;

CREATE TABLE log_request  (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    date DATETIME NOT NULL,
    ip VARCHAR(40) NOT NULL,
    request VARCHAR(100) NOT NULL,
    status INT(3) NOT NULL,
    user_agent VARCHAR(300) NOT NULL
);

