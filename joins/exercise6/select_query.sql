SELECT DISTINCT m.firstname || ' ' || m.surname AS member, f.name AS facility
FROM cd.bookings book
         INNER JOIN cd.facilities f ON book.fac_id = f.fac_id
         INNER JOIN cd.members m ON book.mem_id = m.mem_id
WHERE f.name IN ('Tennis Court 1', 'Tennis Court 2')
ORDER BY member, facility;