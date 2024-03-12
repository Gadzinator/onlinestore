CREATE TABLE cd.members
(
    mem_id         INTEGER PRIMARY KEY,
    surname        VARCHAR(200),
    firstname      VARCHAR(200),
    address        VARCHAR(300),
    zipcode        INTEGER,
    telephone      VARCHAR(20),
    recommended_by INTEGER,
    join_date      TIMESTAMP
);