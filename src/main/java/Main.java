import java.sql.*;
import java.io.Console;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/employeeData";
        Console console = System.console();

        String empID = console.readLine("Enter your ID: ");
        char[] password = console.readPassword("Enter your password: ");

        try (Connection connection = openDB(url, empID, password)) {
            int empIDInt = Integer.parseInt(empID);
            setupSession(connection, empIDInt);
            AppUI app = new AppUI(connection);
            app.start();
        } catch (Exception e) {
            System.err.println("ERROR: No suitable login found");
        } finally {}
    }

    private static Connection openDB(String url, String empID, char[] password) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(url, empID, new String(password));
            return connection;
        } finally {
            // Destroy the password in memory; note that the password is passed by reference,
            // so this doesn't need to be repeated in main()
            java.util.Arrays.fill(password, '\0');
        }
    }

    private static void setupSession(Connection conn, int empIDInt) throws SQLException {
        String sqlGetName = "SELECT Fname, Lname " +
                            "FROM employees WHERE empid = ?";

        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(sqlGetName)) {
            ps.setInt(1, empIDInt);
            rs = ps.executeQuery();

            rs.next();
            System.out.printf("Login successful. Welcome, %s %s.%n", rs.getString("Fname"), rs.getString("Lname"));
        } finally {}

        String sqlSetSession = "SET @session_empid = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlSetSession)) {
            ps.setInt(1, empIDInt);
            ps.execute();
        } finally {}
    }
}
