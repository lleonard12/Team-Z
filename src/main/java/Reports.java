import java.sql.*;
import java.util.Scanner;

public class Reports {
    public static void reportPayStatementHistory(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter employee ID: ");
            int empID = Integer.parseInt(scanner.nextLine().strip());

            String sql = "SELECT pay_date, earnings, fed_tax, state_tax, health_care " +
                        "FROM payroll " +
                        "WHERE EMPID = ? " +
                        "ORDER BY pay_date ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, empID);
            ResultSet rs = ps.executeQuery();

            System.out.println("Pay Statement History: ");
            while (rs.next()) {
                System.out.printf("Date: %s | Earnings: %.2f | Federal Tax: %.2f | State Tax: %.2f%n",
                    rs.getDate("pay_date"),
                    rs.getDouble("earnings"),
                    rs.getDouble("fed_tax"),
                    rs.getDouble("state_tax"),
                    rs.getDouble("health_care"));
            }
            rs.close();
            ps.close();
        } catch (SQLException | NumberFormatException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public static void reportByJobTitle(Connection conn, Scanner scanner) {
        try {
            String sql = "SELECT jt.job_title, SUM(p.earnings) AS total_earnings " +
                         "FROM payroll p " +
                         "JOIN employees e ON p.empid = e.empid " +
                         "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                         "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                         "GROUP BY jt.job_title " +
                         "ORDER BY jt.job_title ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("Total Pay by Job Title: ");
            while (rs.next()) {
                System.out.printf("Job Title: %s | Total Earnings: %.2f%n",
                rs.getString("job_title"),
                rs.getDouble("total_earnings"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static void reportByDivision(Connection conn, Scanner scanner) {
        try {
            String sql = "SELECT d.Name, SUM(p.earnings) AS total_earnings " +
                         "FROM payroll p " +
                         "JOIN employees e ON p.empid = e.empid " +
                         "JOIN employee_division ed ON e.empid = ed.empid " +
                         "JOIN division d ON ed.div_ID = d.ID " +
                         "GROUP BY d.Name " +
                         "ORDER BY d.Name ASC";
    
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
    
            System.out.println("Total Pay by Division:");
            while (rs.next()) {
                System.out.printf("Division: %s | Total Earnings: %.2f%n",
                    rs.getString("Name"),
                    rs.getDouble("total_earnings"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
    

    }
