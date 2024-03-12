SELECT ROW_NUMBER() OVER (ORDER BY join_date) AS row_number, firstname, surname
FROM cd.members
ORDER BY join_date