import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SearchEmployees {
    // All searches retrieve the same information, just with different WHERE
    // clauses. This is the base query, which (due to no parameters) can be
    // concatenated with the clause and ordering
    private static final String baseQuery =
        "SELECT * " +
        "FROM employees e " +
        "JOIN address a ON e.empid = a.empid " +
        "JOIN state s ON a.state_id = s.state_id " +
        "JOIN city c ON a.city_id = c.city_id " +
        "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
        "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " + 
        "JOIN employee_division ed ON e.empid = ed.empid " +
        "JOIN division d ON d.ID = ed.div_ID ";

    public static void search(Connection connection, Scanner scanner) {
        System.out.printf(
            "Search by?%n" +
            "id - Employee ID%n" +
            "name - Employee name%n" +
            "dob - Employee date of birth%n" +
            "ssn - Employee social security number%n" +
            ":> "
        );

        String target = scanner.nextLine().toLowerCase().strip();

        try {
            ArrayList<Employee> result = new ArrayList<Employee>();
            switch (target) {
                case "id":
                    System.out.printf("Enter employee ID:%n:> ");
                    int empID = Integer.parseInt(scanner.nextLine().strip());
                    result = SearchEmployees.byEmpID(connection, empID);
                    break;

                case "name":
                    System.out.printf("Enter first name:%n:> ");
                    String fname = scanner.nextLine().strip();
                    System.out.printf("Enter last name:%n:> ");
                    String lname = scanner.nextLine().strip();
                    result = SearchEmployees.byName(connection, fname, lname);
                    break;

                case "dob":
                    System.out.printf("Enter day (as a number):%n:> ");
                    String day = scanner.nextLine();
                    System.out.printf("Enter month (as a number):%n:> ");
                    String month = scanner.nextLine();
                    System.out.printf("Enter year (as a number):%n:> ");
                    String year = scanner.nextLine();
                    result = SearchEmployees.byDOB(connection, day, month, year);
                    break;

                case "ssn":
                    System.out.printf("Enter SSN:%n:> ");
                    // Format the SSN: reduce it, then insert the dashes
                    String ssn = scanner.nextLine().strip().replace(" ", "").replace("-", "");

                    if (ssn.length() != 9) {
                        System.err.println("ERROR: Invalid SSN entry");
                        return;
                    }

                    ssn = ssn.substring(0,3) + "-" +
                          ssn.substring(3,5) + "-" + 
                          ssn.substring(5);
                    System.out.println(ssn);
                    result = SearchEmployees.bySSN(connection, ssn);
                    break;

                default:
                    System.err.printf("ERROR: Unrecognized input %s%n", target);
                    return;
            }

            int len = result.size();
            System.out.printf("%d matches found.%n", len);
            if (len == 0) {
                return;
            }

            System.out.println("Enter an ID to view more info, or nothing to go back:");
            HashMap<String, Employee> hm = prepareMap(result);
            for (Employee e : result) {
                System.out.printf("%d: %s %s%n", e.getID(), e.getFname(), e.getLname());
            }

            System.out.print(":> ");
            String selected = scanner.nextLine();
            Employee e = hm.get(selected);
            if (e == null) {
                return;
            }

            printEmployee(e);
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private static void printEmployee(Employee e) {
        System.out.printf(
            "%n--------------------------------------------------------%n" +
            "%s %s, employee ID #%09d%n" +
            "Salary: $%,d%n" +
            "Hired on: %tD%n" +
            "Date of birth: %tD%n" +
            "Phone number: %s%n" +
            "Email address: %s%n" +
            "SSN: %s%n" +
            "Home address: %s, %s, %s %s%n" + // Street, city, state, zip
            "Division: %s%n" +
            "Job title: %s%n" +
            "--------------------------------------------------------%n",
            e.getFname(), e.getLname(), e.getID(),
            e.getSalary(),
            e.getHireDate(),
            e.getDob(),
            e.getPhoneNumber(),
            e.getEmail(),
            e.getSsn(),
            e.getStreet(), e.getCityName(), e.getStateCode(), e.getZip(),
            e.getDivisionName(),
            e.getJobTitle()
        );
    }

    // We convert the empID to a string instead of using the raw int to more
    // easily compare it to the user's input
    private static HashMap<String, Employee> prepareMap(ArrayList<Employee> employeeList) {
        HashMap<String, Employee> hm = new HashMap<String, Employee>();

        for (Employee e : employeeList) {
            hm.put(String.valueOf(e.getID()), e);
        }

        return hm;
    }

    private static ArrayList<Employee> byEmpID(Connection conn, int empID)
    throws SQLException {
        String sql = baseQuery +
                     "WHERE e.empid = ? AND has_access(e.empid) " +
                     "ORDER BY e.empid ";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empID);
            ResultSet rs = ps.executeQuery();

            ArrayList<Employee> res = new ArrayList<Employee>();
            while (rs.next()) {
                res.add(new Employee(rs));
            }

            return res;
        }  finally {}
    }

    private static ArrayList<Employee> byName(Connection conn, String fname, String lname)
    throws SQLException {
        String sql = baseQuery +
                     "WHERE e.Fname LIKE ?" +
                     "AND e.Lname LIKE ?" +
                     "AND has_access(e.empid) " +
                     "ORDER BY e.empid ";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Setup fuzzy query
            fname = "%" + fname + "%";
            lname = "%" + lname + "%";
            ps.setString(1, fname);
            ps.setString(2, lname);
            ResultSet rs = ps.executeQuery();

            ArrayList<Employee> res = new ArrayList<Employee>();
            while (rs.next()) {
                res.add(new Employee(rs));
            }

            return res;
        }  finally {}
    }

    private static ArrayList<Employee> byDOB(
        Connection conn,
        String day,
        String month,
        String year
    ) throws SQLException, NumberFormatException {
        // Dynamically build the query; empty fields are generic
        StringBuilder sql = new StringBuilder(
            baseQuery + "WHERE has_access(e.empid) "
        );

        boolean has_day = !day.isBlank();
        boolean has_month = !month.isBlank();
        boolean has_year = !year.isBlank();

        if (has_day) {
            sql.append("AND DAY(a.DOB) = ? ");
        }

        if (has_month) {
            sql.append("AND MONTH(a.DOB) = ? ");
        }

        if (has_year) {
            sql.append("AND YEAR(a.DOB) = ? ");
        }

        sql.append("ORDER BY e.empid ");

        int i = 1;
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (has_day) {
                ps.setInt(i++, Integer.parseInt(day));
            }

            if (has_month) {
                ps.setInt(i++, Integer.parseInt(month));
            }

            if (has_year) {
                ps.setInt(i++, Integer.parseInt(year));
            }

            ResultSet rs = ps.executeQuery();

            ArrayList<Employee> res = new ArrayList<Employee>();
            while (rs.next()) {
                res.add(new Employee(rs));
            }

            return res;
        } finally {}
        // SQLExceptions and NumberFormatExceptions are passed to the caller
    }

    private static ArrayList<Employee> bySSN(Connection conn, String SSN) throws SQLException {
        String sql = baseQuery +
                     "WHERE e.SSN = ? AND has_access(e.empid) " +
                     "ORDER BY e.empid ";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, SSN);
            ResultSet rs = ps.executeQuery();

            ArrayList<Employee> res = new ArrayList<Employee>();
            while (rs.next()) {
                res.add(new Employee(rs));
            }

            return res;
        }  finally {}
    }
}
