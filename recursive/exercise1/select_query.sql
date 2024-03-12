WITH RECURSIVE recommenders(recommender) AS (SELECT recommended_by
                                             FROM cd.members
                                             WHERE mem_id = 7
                                             UNION ALL

                                             SELECT m.recommended_by
                                             FROM recommenders rec
                                                      INNER JOIN cd.members m ON m.mem_id = rec.recommender)

SELECT rec.recommender, m.firstname, m.surname
FROM recommenders rec
         INNER JOIN cd.members m ON rec.recommender = m.mem_id
ORDER BY mem_id DESC;