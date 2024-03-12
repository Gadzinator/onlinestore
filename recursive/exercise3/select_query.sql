WITH RECURSIVE recommenders(recommender, member) AS (SELECT recommended_by, mem_id
                                                     FROM cd.members

                                                     UNION ALL
                                                     SELECT m.recommended_by, rec.member

                                                     FROM recommenders rec
                                                              INNER JOIN cd.members m ON m.mem_id = rec.recommender)
SELECT rec.member member, rec.recommender, m.firstname, m.surname
FROM recommenders rec
         INNER JOIN cd.members m ON rec.recommender = m.mem_id
WHERE rec.member = 22
   OR rec.member = 12
ORDER BY rec.member ASC, rec.recommender DESC