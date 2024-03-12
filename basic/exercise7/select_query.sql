SELECT name,
       CASE
           WHEN guest_cost >= 25 THEN 'expensive'
           ELSE 'cheap'
           END AS cost
FROM cd.facilities;