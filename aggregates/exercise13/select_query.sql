SELECT DISTINCT m.surname, m.firstname, m.mem_id, MIN(b.start_time) AS starttme
FROM cd.bookings b
         INNER JOIN cd.members m ON b.mem_id = m.mem_id
WHERE b.start_time >= '2012-07-01'
GROUP BY m.mem_id, m.firstname, m.surname
ORDER BY m.mem_id