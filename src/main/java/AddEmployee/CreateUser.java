package AddEmployee;

import java.sql.*;

class CreateUser {
    public static void createUser(
        Connection conn,
        String username,
        String password,
        String roleString 
    ) throws SQLException, IllegalArgumentException {
        String sqlMakeUser    = "CREATE USER ?@localhost IDENTIFIED BY ?;";
        String sqlGrantRole   = "GRANT ? TO ?@localhost;";
        String sqlDefaultRole = "SET DEFAULT ROLE ? TO ?@localhost;";

        String role;
        switch (roleString.toLowerCase().strip()) {
            case "admin":
                role = "admin_role";
                break;

            case "employee":
                role = "employee_role";
                break;

            default:
                throw new IllegalArgumentException(
                    "Role must be 'admin' or 'employee', got: " + roleString
                );
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlMakeUser)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlGrantRole)) {
            ps.setString(1, role);
            ps.setString(2, username);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlDefaultRole)) {
            ps.setString(1, role);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }
}
