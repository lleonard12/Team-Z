import java.sql.*;
import java.util.Scanner;

public class AppUI {
    Connection connection;
    Scanner scanner = new Scanner(System.in);

    AppUI(Connection connection) {
        this.connection = connection;
    }

    public void start() {
        String inputString;

        System.out.println("Enter 'help' for help info.");

        // Take user input and process it in a loop
        mainloop:
        while (true) {
            System.out.print(":> ");
            inputString = scanner.nextLine();

            switch (inputString) {
                case "quit":
                    System.out.println("Quitting...");
                    break mainloop;

                case "help":
                    help();
                    break;

                case "search":
                    SearchEmployees.search(connection, scanner);
                    break;

                case "report":
                    report();
                    break;

                case "edit":
                    edit();
                    break;

                case "salaries":
                    try {
                        salaries();
                    } catch (SQLException e) {
                        System.err.println("ERROR: " + e);
                    }
                    break;

                case "adduser":
                    addUser();
                    break;

                case "deleteuser":
                    deleteUser();
                    break;

                default:
                    System.err.printf("ERROR: Unrecognized command '%s'%n" +
                                      "Please enter 'help' for help.%n", inputString);
            }
        }

        scanner.close();
    }

    void help() {
        System.out.printf(
            "-------------------------------------------------------------------------------%n" +
            "Employee management system by Team-Z.%n%n" +

            "Commands:%n%n" +

            "All users:%n" +
            "quit - Exit the application.%n" +
            "help - Print this help information.%n" +
            "search - Search for employees. Non-admins may only view their own data.%n" +
            "report - Generate reports on employee data. Some reports are admin-only.%n%n" +

            "Admins only:%n" +
            "edit - Edit employee data.%n" +
            "salaries - Update employee salaries in range by %%.%n" +
            "adduser - Create a new user.%n" +
            "deleteuser - Remove a user from the database.%n" +
            "-------------------------------------------------------------------------------%n"
        );
    }

    private void report() {
        System.out.println("Select a report to generate: ");
        System.out.println("1 - Pay statement history");
        System.out.println("2- Total pay for month by job title");
        System.out.println("3 - Total pay for month by division");
        System.out.print(":>");

        Sttring choice = scanner.nextLine().strip();

        switch (choice) {
            case "1":
                Reports.reportPayStatementHistory(connection, scanner);
                break;
            case "2":
                Reports.reportByJobTitle(connection, scanner);
                break;
            case "3":
                Reports.reportByDivision(connection, scanner);
                break;
            default:
                System.out.println("Invalid report selection.");
                break;
    }

    private void edit() {
        ;
    }

    private void salaries() throws SQLException {
        // I chose to round the salary amount because, even though the salary
        // field is of 'decimal' type, I don't want automated queries like this
        // to pollute the salaries column with non-integer values.
        String sql = "UPDATE employees " +
                     "SET Salary = ROUND(Salary * (1 + ?/100)) " +
                     "WHERE Salary BETWEEN ? AND ? ";
        try {
            System.out.printf("Enter percent to raise by:%n:> ");
            double raisePercent = Double.parseDouble(scanner.nextLine());

            System.out.printf("Enter lower salary bound (inclusive):%n:> ");
            int lower = Integer.parseInt(scanner.nextLine());

            System.out.printf("Enter upper salary bound (inclusive):%n:> ");
            int upper = Integer.parseInt(scanner.nextLine());

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, raisePercent);
            ps.setInt(2, lower);
            ps.setInt(3, upper);
            int rowsUpdated = ps.executeUpdate();

            System.out.printf("Updated %d employees.%n", rowsUpdated);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Could not format input value");
        }
    }

    private void addUser() {
        ;
    }

    private void deleteUser() {
        ;
    }
}
