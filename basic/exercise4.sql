CREATE TABLE cd.facilities
(
    fac_id              INTEGER PRIMARY KEY,
    name                VARCHAR(200),
    member_cost         INTEGER,
    guest_cost          INTEGER,
    initial_outlay      INTEGER,
    monthly_maintenance INTEGER
);

INSERT INTO cd.facilities
VALUES (0, 'Tennis Court 1', 5, 25, 10000, 200),
       (1, 'Tennis Court 2', 5, 25, 8000, 200),
       (2, 'Badminton Court', 0, 15.5, 4000, 50),
       (3, 'Table Tennis', 0, 5, 320, 10),
       (4, 'Massage Room 1', 35, 80, 4000, 3000),
       (5, 'Massage Room 2', 35, 80, 4000, 3000),
       (6, 'Squash Court', 3.5, 17.5, 5000, 80),
       (7, 'Snooker Table', 0, 5, 450, 15),
       (8, 'Pool Table', 0, 5, 400, 15);

COMMIT;

SELECT fac_id, name, member_cost, monthly_maintenance
FROM cd.facilities
WHERE member_cost > 0
  AND member_cost < monthly_maintenance / 50.0;