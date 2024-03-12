SELECT m.firstname,
       m.surname,
       ROUND(SUM(b.slots) / 2, -1)                             AS hours,
       RANK() OVER (ORDER BY ROUND(SUM(b.slots) / 2, -1) DESC) AS rank
FROM cd.members m
         INNER JOIN
     cd.bookings b ON m.mem_id = b.mem_id
GROUP BY m.mem_id, m.firstname, m.surname
ORDER BY rank, surname, firstname;