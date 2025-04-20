import java.sql.*;
import java.util.HashMap;
import java.util.function.Function;

public class EditEmployees {
    // We need a way to associate the type of the column we're modifying
    // so we know how to parse the new value passed to updateField.
    // We also need a way to determine whether the column name is valid.
    // Solution: Enum of valid column names and their types; if the
    // column name is a variant in the enum, it's valid, and its type is the
    // associated value. We also give each variant a parser lambda to easily
    // get its correct value.
    public enum EmployeeColumn {
        // TODO: The rest of the editable columns, plus some mechanism for different tables
        FNAME("Fname", "string", val -> val),
        LNAME("Lname", "string", val -> val),
        EMAIL("email", "string", val -> val),
        SALARY("salary", "int", val -> Integer.parseInt(val));

        private final String columnName;
        private final String type;
        private final Function<String, Object> parser;

        EmployeeColumn(String columnName, String type, Function<String, Object> parser) {
            this.columnName = columnName;
            this.type = type;
            this.parser = parser;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getType() {
            return type;
        }

        public static EmployeeColumn fromColumnName(String fieldToUpdate) {
            for (EmployeeColumn column : values()) {
                if (column.columnName.equals(fieldToUpdate)) {
                    return column;
                }
            }

            // If we get here, the column name was never found, i.e. it doesn't exist
            throw new IllegalArgumentException("Invalid column name: " + fieldToUpdate);
        }

        public Object parseValue(String value) {
            return parser.apply(value);
        }
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
        EmployeeColumn column = EmployeeColumn.fromColumnName(fieldToUpdate);

        String sql = "UPDATE employees " +
                     "SET " + fieldToUpdate + " = ? " +
                     "WHERE empid = ? ";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, empID);
            // Convert the given string value into an Object of the appropriate
            // type, as determined by the internal parser. This way, setObject
            // can determine the type we're passing. While we could rely on
            // MySQL's type coercion, I would rather make it explicit.
            ps.setObject(1, column.parseValue(newVal));
            return ps.executeUpdate();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid value '" + newVal +
                "' for integer column: " + fieldToUpdate
            );
        }
        // We don't catch the SQLExceptions explicitly since
        // they're supposed to go up the stack anyways
    }
}
