SELECT member, facility, cost
FROM (SELECT mems.firstname || ' ' || mems.surname AS member,
             f.name                                AS facility,
             CASE
                 WHEN mems.mem_id = 0 THEN
                     book.slots * f.guest_cost
                 ELSE
                     book.slots * f.member_cost
                 END                               AS cost
      FROM cd.members mems
               INNER JOIN cd.bookings book
                          ON mems.mem_id = book.mem_id
               INNER JOIN cd.facilities f
                          ON book.fac_id = f.fac_id

      WHERE book.start_time >= '2012-07-03'
        AND book.start_time < '2012-07-04') AS bookings
WHERE cost > 30
ORDER BY cost DESC;