import java.sql.*;
import java.util.Scanner;

public class AppUI {
    Connection connection;

    AppUI(Connection connection) {
        this.connection = connection;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.printf("Enter 'help' for help info.%n");

        // Take user input and process it in a loop
        mainloop:
        while (true) {
            System.out.print(":> ");
            input = scanner.nextLine();

            switch (input) {
                case "quit":
                    System.out.println("Quitting...");
                    break mainloop;

                case "help":
                    help();
                    break;
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
            "-------------------------------------------%n"
        );
    }
}
