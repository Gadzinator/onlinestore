SELECT f.fac_id,
       f.name,
       TRIM(TO_CHAR(SUM(b.slots) / 2.0, '999D99')) AS "Total Hours"
FROM cd.facilities f
         INNER JOIN cd.bookings b ON f.fac_id = b.fac_id
GROUP BY f.fac_id
ORDER BY f.fac_id