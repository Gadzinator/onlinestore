SELECT DISTINCT m.firstname || ' ' || m.surname AS member,
                r.firstname || ' ' || r.surname AS recommender
FROM cd.members m
         LEFT JOIN cd.members r ON m.recommended_by = r.mem_id
ORDER BY member;