import java.sql.*;
import java.util.HashMap;

public class EditEmployees {
    // We need a way to associate the type of the column we're modifying
    // so we know how to parse the new value passed to updateField.
    // We also need a way to determine whether the column name is valid.
    // Solution: Hashmap of valid column names and their types; if the
    // column name is a key in the map, it's valid, and its type is the
    // associated value, which we can switch against to resolve.
    private static final HashMap<String, String> columnTypes = new HashMap<>();
    // TODO: Fill out the rest of the editable columns
    static {
        columnTypes.put("FName", "string");
        columnTypes.put("LName", "string");
        columnTypes.put("email", "string");
        columnTypes.put("salary", "int");
    }

    public static int updateField(
        Connection conn,
        int empID,
        String fieldToUpdate,
        String newVal
    ) throws SQLException {
        // SAFETY NOTE: There is no way to inject a column name via a
        // parameterized query, so direct concatenation is our only option.
        // However, not only are the column names that get passed to this
        // predetermined, we also validate them anyways; so this is safe.
        String resolvedType = columnTypes.get(fieldToUpdate);
        if (resolvedType == null) {
            throw new IllegalArgumentException("Invalid column name: " + fieldToUpdate);
        }

        PreparedStatement ps = null;
        String sql = "UPDATE employees " +
                     "SET " + fieldToUpdate + " = ? " +
                     "WHERE empid = ? ";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(2, empID);

            switch (resolvedType) {
                case "string":
                    ps.setString(1, newVal);
                    break;

                case "int":
                    ps.setInt(1, Integer.parseInt(newVal));
                    break;
            }

            int rowsUpdated;
            rowsUpdated = ps.executeUpdate();
            return rowsUpdated;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid value '" + newVal +
                "' for integer colum: " + fieldToUpdate
            );
        } finally {
            if (ps != null) ps.close();
        }
        // We don't catch the SQLExceptions explicitly since
        // they're supposed to go up the stack anyways
    }
}
