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

                default:
                    System.out.printf("ERROR: Unrecognized command '%s'%n" +
                                      "Please enter 'help' for help.%n", inputString);
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
