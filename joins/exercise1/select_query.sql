SELECT b.start_time
FROM cd.bookings b
         INNER JOIN cd.members m ON b.mem_id = m.mem_id
WHERE m.surname = 'Farrell'
  AND m.firstname = 'David';