SELECT b.start_time AS start, f.name
FROM cd.bookings b
         INNER JOIN cd.members m ON b.mem_id = m.mem_id
         INNER JOIN cd.facilities f ON b.fac_id = f.fac_id
WHERE f.name LIKE 'Tennis Court%'
  AND b.start_time >= '2012-09-21'
  AND b.start_time <= '2012-09-22'
ORDER BY b.start_time