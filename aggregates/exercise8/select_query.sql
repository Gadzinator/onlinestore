SELECT f.name,
       SUM(CASE
               WHEN b.mem_id = 0 THEN b.slots * f.guest_cost
               ELSE b.slots * f.member_cost END) AS revenue
FROM cd.bookings b
         INNER JOIN cd.facilities f ON b.fac_id = f.fac_id
GROUP BY f.name
ORDER BY revenue