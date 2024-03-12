SELECT fac_id, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY fac_id
HAVING SUM(slots) > 8
ORDER BY fac_id;