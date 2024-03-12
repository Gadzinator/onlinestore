SELECT join_date AS latest
FROM cd.members
ORDER BY join_date DESC
LIMIT 1;