GRANT ALL PRIVILEGES ON *.* TO 'admin_role';
GRANT SELECT ON employeeData.* TO 'employee_role';
GRANT EXECUTE ON FUNCTION has_access TO 'employee_role';

CREATE USER '000000001'@localhost IDENTIFIED BY 'password';
GRANT 'admin_role' TO '000000001'@localhost;
SET DEFAULT ROLE 'admin_role' TO '000000001'@localhost;

CREATE USER '000000002'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000002'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000002'@localhost;

CREATE USER '000000003'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000003'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000003'@localhost;

CREATE USER '000000004'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000004'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000004'@localhost;

CREATE USER '000000005'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000005'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000005'@localhost;

CREATE USER '000000006'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000006'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000006'@localhost;

CREATE USER '000000007'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000007'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000007'@localhost;

CREATE USER '000000008'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000008'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000008'@localhost;

CREATE USER '000000009'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000009'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000009'@localhost;

CREATE USER '000000010'@localhost IDENTIFIED BY 'password';
GRANT 'admin_role' TO '000000010'@localhost;
SET DEFAULT ROLE 'admin_role' TO '000000010'@localhost;

CREATE USER '000000011'@localhost IDENTIFIED BY 'password';
GRANT 'admin_role' TO '000000011'@localhost;
SET DEFAULT ROLE 'admin_role' TO '000000011'@localhost;

CREATE USER '000000012'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000012'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000012'@localhost;

CREATE USER '000000013'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000013'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000013'@localhost;

CREATE USER '000000014'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000014'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000014'@localhost;

CREATE USER '000000015'@localhost IDENTIFIED BY 'password';
GRANT 'employee_role' TO '000000015'@localhost;
SET DEFAULT ROLE 'employee_role' TO '000000015'@localhost;