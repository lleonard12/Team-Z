import java.sql.*;
import java.util.function.Function;

public class EditEmployees {
    private interface Column {
        Object parseValue(String value);
    }

    // We need a way to associate the type of the column we're modifying
    // so we know how to parse the new value passed to updateField.
    // We also need a way to determine whether the column name is valid.
    // Solution: Enum of valid column names and their types; if the
    // column name is a variant in the enum, it's valid, and its type is the
    // associated value. We also give each variant a parser lambda to easily
    // get its correct value.
    public enum EmployeeColumn implements Column {
        FNAME("Fname", "string", val -> val),
        LNAME("Lname", "string", val -> val),
        EMAIL("email", "string", val -> val),
        SALARY("salary", "int", val -> Integer.parseInt(val)),
        SSN("SSN", "string", val -> val);

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
                if (column.getColumnName().equalsIgnoreCase(fieldToUpdate)) {
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

    public enum AddressColumn implements Column {
        STREET("street", "string", val -> val),
        ZIP("zip", "string", val -> val),
        GENDER("gender", "string", val -> val),
        RACE("identified_race", "int", val -> val),
        PHONE_NUMBER("phone_number", "string", val -> val),
        CITY_ID("city_id", "int", val -> Integer.parseInt(val)),
        STATE_ID("state_id", "int", val -> Integer.parseInt(val));

        private final String columnName;
        private final String type;
        private final Function<String, Object> parser;

        AddressColumn(String columnName, String type, Function<String, Object> parser) {
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

        public static AddressColumn fromColumnName(String fieldToUpdate) {
            for (AddressColumn column : values()) {
                if (column.getColumnName().equalsIgnoreCase(fieldToUpdate)) {
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

    public enum Table {
        EMPLOYEES("employees"),
        ADDRESS("address");

        private final String tableName;

        Table(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }

        public static Table fromTableName(String tableName) {
            for (Table table : values()) {
                if (table.getTableName().equalsIgnoreCase(tableName)) {
                    return table;
                }
            }

            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
    }

    public static int updateField(
        Connection conn,
        int empID,
        String tableToUpdate,
        String fieldToUpdate,
        String newVal
    ) throws SQLException, IllegalArgumentException {
        Table table = Table.fromTableName(tableToUpdate);

        // SAFETY NOTE: There is no way to inject a column name via a
        // parameterized query, so direct concatenation is our only option.
        // However, not only are the column names that get passed to this
        // predetermined, we also validate them anyways; so this is safe.
        Column column;
        switch (table.getTableName()) {
            case "employees":
                column = EmployeeColumn.fromColumnName(fieldToUpdate);
                break;

            case "address":
                column = AddressColumn.fromColumnName(fieldToUpdate);
                break;

            default:
                throw new IllegalArgumentException("Invalid table name");
        }

        String sql = "UPDATE " + table.getTableName() +
                     " SET " + fieldToUpdate + " = ? " +
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
