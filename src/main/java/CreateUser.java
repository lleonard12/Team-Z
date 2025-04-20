import java.sql.*;

// Create a new user in the database
public class CreateUser {
    public enum EmployeeRole {
        EMPLOYEE("employee_role"),
        ADMIN("admin_role");

        private final String role;

        EmployeeRole(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    public static void createUser(
        Connection conn,
        String username,
        String password,
        EmployeeRole role 
    ) throws SQLException {
        String sqlMakeUser    = "CREATE USER ?@localhost IDENTIFIED BY ?;";
        String sqlGrantRole   = "GRANT ? TO ?@localhost;";
        String sqlDefaultRole = "SET DEFAULT ROLE ? TO ?@localhost;";

        try (PreparedStatement ps = conn.prepareStatement(sqlMakeUser)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlGrantRole)) {
            ps.setString(1, role.getRole());
            ps.setString(2, username);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlDefaultRole)) {
            ps.setString(1, role.getRole());
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }
}
