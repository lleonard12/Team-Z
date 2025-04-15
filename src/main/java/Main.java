import java.sql.*;
import java.io.Console;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = openDB()) {
            AppUI app = new AppUI(connection);
            app.start();
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        } finally {}
    }

    static Connection openDB() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/employeeData";
        Console console = System.console();

        String username = console.readLine("Enter your username: ");
        char[] password = console.readPassword("Enter your password: ");

        System.out.println("Login successful. Welcome, " + username + ".");

        try {
            Connection connection = DriverManager.getConnection(url, username, new String(password));
            return connection;
        } finally {
            // Destroy the password in memory
            java.util.Arrays.fill(password, ' ');
        }
    }
}
