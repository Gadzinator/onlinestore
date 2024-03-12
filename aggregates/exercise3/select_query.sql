SELECT recommended_by, COUNT(*)
FROM cd.members
WHERE recommended_by IS NOT NULL
GROUP BY recommended_by
ORDER BY recommended_by;