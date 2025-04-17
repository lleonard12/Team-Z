import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AppUI {
    Connection connection;

    AppUI(Connection connection) {
        this.connection = connection;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String inputString;
        int inputInt;

        System.out.printf("Enter 'help' for help info.%n");

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

                // TODO: Temporary case to test permissions. Replace this with a unified search command later
                case "search":
                    try {
                        inputInt = scanner.nextInt();
                        SearchEmployees.byEmpId(connection, inputInt);
                    } catch (SQLException e) {
                        System.out.println("ERROR: " + e);
                    } catch (InputMismatchException e) {
                        System.out.println("ERROR: " + e);
                    }
                    break;

                // TODO: Temporary case to test permissions, blah blah same thing as above
                case "editTEMP":
                    try {
                        EditEmployees.updateField(connection, 1, "FName", "Snoopy");
                    } catch (SQLException e) {
                        System.out.println("ERROR: " + e);
                    } catch (InputMismatchException e) {
                        System.out.println("ERROR: " + e);
                    }
                    break;

                default:
                    System.out.printf("ERROR: Unrecognized command '%s'%n", inputString,
                                      "Please enter 'help' for help.");
            }
        }

        scanner.close();
    }

    void help() {
        System.out.printf(
            "-------------------------------------------%n" +
            "Employee management system by Team-Z.%n%n"+
            "Commands:%n"+
            "quit - Exit the application.%n" +
            "help - Print this help information.%n" +
            "search - Search for employees%n" +
            "edit - Edit employee data%n" +
            "-------------------------------------------%n"
        );
    }
}
