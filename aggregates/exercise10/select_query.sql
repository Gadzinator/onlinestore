SELECT fac_id, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY fac_id
ORDER BY "Total Slots" DESC
LIMIT 1