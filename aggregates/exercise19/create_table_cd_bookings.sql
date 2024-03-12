CREATE TABLE cd.bookings
(
    booking_id SERIAL PRIMARY KEY,
    fac_id     INTEGER,
    mem_id     INTEGER,
    start_time TIMESTAMP,
    slots      INTEGER,
    FOREIGN KEY (fac_id) REFERENCES cd.facilities (fac_id),
    FOREIGN KEY (mem_id) REFERENCES cd.members (mem_id)
);
