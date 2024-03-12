SELECT fac_id, name, member_cost, monthly_maintenance
FROM cd.facilities
WHERE member_cost > 0
  AND member_cost < monthly_maintenance / 50.0;