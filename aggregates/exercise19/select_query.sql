SELECT f.name,
       f.initial_outlay / (SUM(CASE
                                   WHEN mem_id = 0 THEN slots * guest_cost
                                   ELSE slots * member_cost
           END) / 3 - f.monthly_maintenance) AS months
FROM cd.facilities f
         INNER JOIN
     cd.bookings b ON f.fac_id = b.fac_id
GROUP BY f.fac_id, f.name
ORDER BY f.name;