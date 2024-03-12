SELECT DISTINCT m.firstname, m.surname
FROM cd.members m
         INNER JOIN cd.members recomend ON m.mem_id = recomend.recommended_by
ORDER BY surname, firstname;