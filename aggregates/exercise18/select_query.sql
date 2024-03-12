SELECT name,
       RANK() OVER (ORDER BY total_revenue DESC) AS rank
FROM (SELECT f.name,
             SUM(CASE WHEN b.mem_id = 0 THEN b.slots * f.guest_cost ELSE b.slots * f.member_cost END) AS total_revenue
      FROM cd.facilities f
               LEFT JOIN
           cd.bookings b ON f.fac_id = b.fac_id
      GROUP BY f.fac_id, f.name) AS facility_revenue
ORDER BY rank, name
LIMIT 3;