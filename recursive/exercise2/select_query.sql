WITH RECURSIVE recommenders(mem_id) AS (SELECT mem_id
                                        FROM cd.members
                                        WHERE recommended_by = 1

                                        UNION ALL

                                        SELECT m.mem_id
                                        FROM recommenders rec
                                                 INNER JOIN cd.members m ON m.recommended_by = rec.mem_id)

SELECT rec.mem_id, m.firstname, m.surname
FROM recommenders rec
         INNER JOIN cd.members m ON rec.mem_id = m.mem_id
ORDER BY mem_id;