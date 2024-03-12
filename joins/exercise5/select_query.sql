SELECT m.firstname    AS memfname,
       m.surname      AS memsname,
       recs.firstname AS recfname,
       recs.surname   AS recsname
FROM cd.members m
         LEFT OUTER JOIN cd.members recs ON recs.mem_id = m.recommended_by
ORDER BY memsname, memfname;