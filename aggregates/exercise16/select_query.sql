SELECT fac_id, SUM(slots) AS total
FROM cd.bookings
GROUP BY fac_id
ORDER BY total DESC
LIMIT 1