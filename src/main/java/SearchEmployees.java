import java.sql.*;

// TODO: Also get city, state for each employee
public class SearchEmployees {
    public static ResultSet byEmpId(Connection conn, int empID) throws SQLException {
        String sql = "SELECT * " +
                     "FROM employees e " +
                     "JOIN address a ON e.empid = a.empid " +
                     "WHERE e.empid = ? " +
                     "ORDER BY e.empid ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, empID);
            ResultSet res = ps.executeQuery();
            return res;
        }  finally {}
    }

    public static ResultSet byName(Connection conn, String fname, String lname)
    throws SQLException {
        // TODO: Possibly implement fuzzy name matching
        String sql = "SELECT * " +
                     "FROM employees e " +
                     "JOIN address a ON e.empid = a.empid " +
                     "WHERE e.FName = ? AND e.Lname = ? " +
                     "ORDER BY e.FName, e.Lname ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fname);
            ps.setString(2, lname);
            ResultSet res = ps.executeQuery();
            return res;
        }  finally {}
    }

    public static ResultSet byDOB(Connection conn, Date DOB) throws SQLException {
        String sql = "SELECT * " +
                     "FROM employees e " +
                     "JOIN address a ON e.empid = a.empid " +
                     "WHERE a.DOB = ? " +
                     "ORDER BY a.DOB ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, DOB);
            ResultSet res = ps.executeQuery();
            return res;
        }  finally {}
    }

    public static ResultSet bySSN(Connection conn, String SSN) throws SQLException {
        String sql = "SELECT * " +
                     "FROM employees e " +
                     "JOIN address a ON e.empid = a.empid " +
                     "WHERE e.SSN = ? " +
                     "ORDER BY e.SSN ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, SSN);
            ResultSet res = ps.executeQuery();
            return res;
        }  finally {}
    }
}
