import java.sql.*;
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
        "JOIN city c ON a.city_id = c.city_id ";

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
            ResultSet rs;
            switch (target) {
                case "id":
                    System.out.printf("Enter employee ID:%n:> ");
                    int empID = Integer.parseInt(scanner.nextLine().strip());
                    rs = SearchEmployees.byEmpID(connection, empID);
                    break;

                case "name":
                    System.out.printf("Enter first name:%n:> ");
                    String fname = scanner.nextLine().strip();
                    System.out.printf("Enter last name:%n:> ");
                    String lname = scanner.nextLine().strip();
                    rs = SearchEmployees.byName(connection, fname, lname);
                    break;

                case "dob":
                    System.out.printf("Enter day (as a number):%n:> ");
                    String day = scanner.nextLine();
                    System.out.printf("Enter month (as a number):%n:> ");
                    String month = scanner.nextLine();
                    System.out.printf("Enter year (as a number):%n:> ");
                    String year = scanner.nextLine();
                    rs = SearchEmployees.byDOB(connection, day, month, year);
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
                    rs = SearchEmployees.bySSN(connection, ssn);
                    break;

                default:
                    System.err.printf("ERROR: Unrecognized input %s%n", target);
                    return;
            }

            int matchNum = 0;
            while (rs.next()) {
                matchNum++;
                System.out.printf(
                    "%n----------------------------------------------%n" +
                    "MATCH #%d:%n" +
                    "%s %s, employee ID #%09d%n" +
                    "Date of birth: %tD%n" +
                    "SSN: %s%n" +
                    "Home address: %s, %s, %s %s%n" + // Street, city, state, zip
                    "----------------------------------------------%n",
                    matchNum,
                    rs.getString("Fname"), rs.getString("Lname"), rs.getInt("empid"),
                    rs.getDate("DOB"),
                    rs.getString("SSN"),
                    rs.getString("street"), rs.getString("city_name"), rs.getString("state_code"), rs.getString("zip")
                );
            }

            if (matchNum == 0) {
                System.out.println("No matches found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: " + e);
        }
        ;
    }

    private static ResultSet byEmpID(Connection conn, int empID) throws SQLException {
        String sql = baseQuery +
                     "WHERE e.empid = ? AND has_access(e.empid)" +
                     "ORDER BY e.empid ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, empID);
            ResultSet res = ps.executeQuery();
            return res;
        }  finally {}
    }

    private static ResultSet byName(Connection conn, String fname, String lname)
    throws SQLException {
        String sql = baseQuery +
                     "WHERE e.Fname LIKE ?" +
                     "AND e.Lname LIKE ?" +
                     "AND has_access(e.empid)" +
                     "ORDER BY e.Fname, e.Lname ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            // Setup fuzzy query
            fname = "%" + fname + "%";
            lname = "%" + lname + "%";
            ps.setString(1, fname);
            ps.setString(2, lname);
            ResultSet res = ps.executeQuery();
            return res;
        }  finally {}
    }

    private static ResultSet byDOB(
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

        sql.append("ORDER BY a.DOB ");

        int i = 1;
        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            if (has_day) {
                ps.setInt(i++, Integer.parseInt(day));
            }

            if (has_month) {
                ps.setInt(i++, Integer.parseInt(month));
            }

            if (has_year) {
                ps.setInt(i++, Integer.parseInt(year));
            }

            ResultSet res = ps.executeQuery();
            return res;
        } finally {}
        // SQLExceptions and NumberFormatExceptions are passed to the caller
    }

    private static ResultSet bySSN(Connection conn, String SSN) throws SQLException {
        String sql = baseQuery +
                     "WHERE e.SSN = ? AND has_access(e.empid)" +
                     "ORDER BY e.SSN ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, SSN);
            ResultSet res = ps.executeQuery();
            return res;
        }  finally {}
    }
}
