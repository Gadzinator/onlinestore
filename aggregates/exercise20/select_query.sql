SELECT dategen.date,
       (SELECT SUM(CASE
                       WHEN mem_id = 0 THEN slots * facs.guest_cost
                       ELSE slots * member_cost
           END) AS rev

        FROM cd.bookings bks
                 INNER JOIN cd.facilities facs
                            ON bks.fac_id = facs.fac_id
        WHERE bks.start_time > dategen.date - INTERVAL '14 days'
          AND bks.start_time < dategen.date + INTERVAL '1 day') / 15 AS revenue
FROM (SELECT CAST(GENERATE_SERIES(TIMESTAMP '2012-07-01',
                                  '2012-07-31', '1 day') AS DATE) AS date) AS dategen
ORDER BY dategen.date;