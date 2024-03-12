SELECT fac_id, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE start_time >= '2012-07-01'
  AND start_time < '2012-08-01'
GROUP BY fac_id
ORDER BY "Total Slots";