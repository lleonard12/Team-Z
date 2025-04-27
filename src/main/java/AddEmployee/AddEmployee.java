package AddEmployee;

import java.time.*;
import java.util.Scanner;
import java.io.Console;
import java.sql.*;

public class AddEmployee {
    // While I would prefer to split this into multiple methods, having
    // it all in one place allows stricter control over when failures occur,
    // which makes it easier to prevent invalid database states.
    public static void addEmployee(Connection conn, Scanner scanner)
    throws SQLException, IllegalArgumentException, DateTimeException {
        // -------------
        // | EMPLOYEES |
        // -------------
        System.out.printf("First name:%n:> ");
        String fname = scanner.nextLine();

        System.out.printf("Last name:%n:> ");
        String lname = scanner.nextLine();

        System.out.printf("Email address:%n:> ");
        String email = scanner.nextLine();

        // HireDate is determined automatically when the employee is added
        LocalDateTime now = LocalDateTime.now();
        Timestamp hireDate = Timestamp.valueOf(now);

        System.out.printf("Salary:%n:> ");
        int salary;
        String salaryString = scanner.nextLine();
        try {
            salary = Integer.parseInt(salaryString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid input for salary: " + salaryString
            );
        }

        System.out.printf("SSN:%n:> ");
        String ssn = scanner.nextLine();

        // -----------
        // | ADDRESS |
        // -----------
        System.out.printf("Street:%n:> ");
        String street = scanner.nextLine();

        System.out.printf("Zip code:%n:> ");
        String zip = scanner.nextLine();

        System.out.printf("Gender:%n:> ");
        String gender = scanner.nextLine();

        System.out.printf("Identified race:%n:> ");
        String race = scanner.nextLine();

        Date dob = getDOB(scanner);

        System.out.printf("Phone number:%n:> ");
        String phoneNumber = scanner.nextLine();

        // TODO?: For city, state, job title, and division ID, there's possible
        // room for improvement over having to blindly insert the number
        System.out.printf("City ID:%n:> ");
        int cityID;
        String cityIDString = scanner.nextLine();
        try {
            cityID = Integer.parseInt(cityIDString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid input for city ID: " + cityIDString
            );
        }

        System.out.printf("State ID:%n:> ");
        int stateID;
        String stateIDString = scanner.nextLine();
        try {
            stateID = Integer.parseInt(stateIDString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid input for state ID: " + stateIDString
            );
        }

        // ---------------
        // | DIVISION ID |
        // ---------------
        System.out.printf("Division ID:%n:> ");
        int divID;
        String divIDString = scanner.nextLine();
        try {
            divID = Integer.parseInt(divIDString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid input for division ID: " + divIDString
            );
        }

        // ----------------
        // | JOB TITLE ID |
        // ----------------
        System.out.printf("Job title ID:%n:> ");
        int jobTitleID;
        String jobTitleIDString = scanner.nextLine();
        try {
            jobTitleID = Integer.parseInt(jobTitleIDString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid input for job title ID: " + jobTitleIDString
            );
        }

        // --------------------
        // | USER INFORMATION |
        // --------------------
        System.out.println("Is this user an ('admin') or an ('employee')?");
        String role = scanner.nextLine();

        // -----------------------------
        // | USER CREATION STARTS HERE |
        // -----------------------------
        // Make sure all the user-creation operations succeed or fail together;
        // atomicity
        // We must fetch the next employee ID to be generated, since
        // that will ultimately become the new employee's username
        conn.setAutoCommit(false);

        String username = null;
        try (
            Statement stmtAnalyzeTable = conn.createStatement();
            Statement stmtGetNextEmpID = conn.createStatement();
        ) {
            // Make sure the AUTO_INCREMENT value is up-to-date
            String sqlAnalyzeTable = "ANALYZE TABLE employees ";
            stmtAnalyzeTable.executeQuery(sqlAnalyzeTable);

            // Get the next auto-increment value for the new employee's account name
            String sqlGetNextEmpID = "SELECT AUTO_INCREMENT " +
                                     "FROM information_schema.TABLES " +
                                     "WHERE TABLE_SCHEMA = 'employeeData' " +
                                     "AND TABLE_NAME = 'employees' ";

            ResultSet rs = stmtGetNextEmpID.executeQuery(sqlGetNextEmpID);
            if (rs.next()) {
                long nextID = rs.getLong("AUTO_INCREMENT");
                username = String.format("%09d", nextID);
            } else {
                throw new SQLException("Could not find next employee ID");
            }

            Console console = System.console();
            char[] password = console.readPassword("Enter the employee's password: ");
            try {
                CreateUser.createUser(conn, username, new String(password), role);
            } finally {
                java.util.Arrays.fill(password, '\0');
            }

            InsertData.setEmployeesTable(
                conn,
                fname,
                lname,
                email,
                hireDate,
                salary,
                ssn
            );

            InsertData.setAddressTable(
                conn, 
                street, 
                zip, 
                gender,
                race,
                dob,
                phoneNumber,
                cityID,
                stateID
            );

            InsertData.setDivision(conn, divID);

            InsertData.setJobTitle(conn, jobTitleID);

            System.out.println("Added employee successfully.");
        } catch (SQLException e) {
            // Try to roll back the failed transaction
            try (PreparedStatement ps = conn.prepareStatement("DROP USER IF EXISTS ?@localhost;")) {
                conn.rollback();

                // User is created in the system DB so we have to drop it manually
                if (username != null) {
                    ps.setString(1, username);
                    ps.executeUpdate();
                }
            } catch (SQLException f) {
                System.err.println(
                    "CRITICAL ERROR: Could not revert failed SQL transaction: " +
                    f.getMessage()
                );
            }

            throw new SQLException("User creation failed: " + e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }

    }

    private static Date getDOB(Scanner scanner)
    throws DateTimeException, IllegalArgumentException {
        try {
            System.out.printf("Year of birth:%n:> ");
            String yob = scanner.nextLine();
            int yob_num = Integer.parseInt(yob);

            System.out.printf("Month of birth:%n:> ");
            String mob = scanner.nextLine();
            int mob_num = Integer.parseInt(mob);

            System.out.printf("Day of birth:%n:> ");
            String dob = scanner.nextLine();
            int dob_num = Integer.parseInt(dob);

            LocalDate ld = LocalDate.of(yob_num, mob_num, dob_num);
            return Date.valueOf(ld);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid input for numeric field"
            );
        }
    }
}
