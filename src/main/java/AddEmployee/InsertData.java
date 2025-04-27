package AddEmployee;

import java.sql.*;

class InsertData {
    static void setEmployeesTable(
        Connection conn,
        String fname,
        String lname,
        String email,
        Timestamp hireDate,
        int salary,
        String ssn
    ) throws SQLException {
        // WARNING empid auto-increments, DO NOT override the new index
        String sql = "INSERT INTO employees (Fname, Lname, email, HireDate, Salary, SSN) " +
                     "VALUES " +
                     "(?, ?, ?, ?, ?, ?); ";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, email);
            ps.setTimestamp(4, hireDate);
            ps.setInt(5, salary);
            ps.setString(6, ssn);
            ps.executeUpdate();
        }
    }

    static void setAddressTable(
        Connection conn,
        String street,
        String zip,
        String gender,
        String race,
        Date dob,
        String phoneNumber,
        int cityID,
        int stateID
    ) throws SQLException {
        // WARNING empid auto-increments, DO NOT override the new index
        String sql = "INSERT INTO address (street, zip, gender, identified_race, DOB, phone_number, city_id, state_id) " +
                     "VALUES " +
                     "(?, ?, ?, ?, ?, ?, ?, ?); ";


        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, street);
            ps.setString(2, zip);
            ps.setString(3, gender);
            ps.setString(4, race);
            ps.setDate(5, dob);
            ps.setString(6, phoneNumber);
            ps.setInt(7, cityID);
            ps.setInt(8, stateID);
            ps.executeUpdate();
        }
    }

    static void setJobTitle(
        Connection conn, 
        int jobTitleID
    ) throws SQLException {
        // WARNING empid auto-increments, DO NOT override the new index
        String sql = "INSERT INTO employee_job_titles (job_title_id) VALUES (?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobTitleID);
            ps.executeUpdate();
        }
    }

    static void setDivision(
        Connection conn,
        int divisionID
    ) throws SQLException {
        // WARNING empid auto-increments, DO NOT override the new index
        String sql = "INSERT INTO employee_division (div_ID) VALUES (?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, divisionID);
            ps.executeUpdate();
        }
    }
}
