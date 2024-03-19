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

INSERT INTO cd.bookings (fac_id, mem_id, start_time, slots)
VALUES (3, 1, '2012-07-03 11:00:00', 2),
       (4, 1, '2012-07-03 08:00:00', 2),
       (6, 0, '2012-07-03 18:00:00', 2),
       (7, 1, '2012-07-03 19:00:00', 2),
       (8, 1, '2012-07-03 10:00:00', 1),
       (8, 1, '2012-07-03 15:00:00', 1),
       (0, 2, '2012-07-04 09:00:00', 3),
       (0, 2, '2012-07-04 15:00:00', 3),
       (4, 3, '2012-07-04 13:30:00', 2),
       (4, 0, '2012-07-04 15:00:00', 2),
       (4, 0, '2012-07-04 17:30:00', 2),
       (6, 0, '2012-07-04 12:30:00', 2),
       (6, 0, '2012-07-04 14:00:00', 2),
       (6, 1, '2012-07-04 15:30:00', 2),
       (7, 2, '2012-07-04 14:00:00', 2),
       (8, 2, '2012-07-04 12:00:00', 1),
       (8, 3, '2012-07-04 18:00:00', 1),
       (1, 0, '2012-07-05 17:30:00', 3),
       (2, 1, '2012-07-05 09:30:00', 3),
       (3, 3, '2012-07-05 09:00:00', 2),
       (3, 1, '2012-07-05 19:00:00', 2),
       (4, 3, '2012-07-05 18:30:00', 2),
       (6, 0, '2012-07-05 13:00:00', 2),
       (6, 1, '2012-07-05 14:30:00', 2),
       (7, 2, '2012-07-05 18:30:00', 2),
       (8, 3, '2012-07-05 12:30:00', 1);

COMMIT;

SELECT fac_id,
       EXTRACT(MONTH FROM start_time) AS month,
       SUM(slots)                     AS slots
FROM cd.bookings
WHERE start_time >= '2012-07-03'
  AND start_time < '2012-07-04'
GROUP BY ROLLUP (fac_id, month)
ORDER BY fac_id, month;