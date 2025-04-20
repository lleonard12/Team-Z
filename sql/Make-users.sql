CREATE ROLE 'admin_role', 'employee_role';

GRANT ALL PRIVILEGES ON employeeData.* TO 'admin_role';
GRANT SELECT ON employeeData.* TO 'employee_role';
GRANT EXECUTE ON FUNCTION has_access TO 'employee_role';