CREATE TABLE cd.facilities
(
    fac_id              INTEGER PRIMARY KEY,
    name                VARCHAR(200),
    member_cost         INTEGER,
    guest_cost          INTEGER,
    initial_outlay      INTEGER,
    monthly_maintenance INTEGER
);