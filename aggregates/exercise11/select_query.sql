SELECT fac_id,
       EXTRACT(MONTH FROM start_time) AS month,
       SUM(slots)                     AS slots
FROM cd.bookings
WHERE start_time >= '2012-01-01'
  AND start_time < '2013-01-01'
GROUP BY ROLLUP (fac_id, month)
ORDER BY fac_id, month;