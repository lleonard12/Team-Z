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
                    salaries();
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
        ;
    }

    private void edit() {
        ;
    }

    private void salaries() {
        ;
    }

    private void addUser() {
        ;
    }

    private void deleteUser() {
        ;
    }
}
