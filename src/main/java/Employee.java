import java.sql.*;

public class Employee {
    private int ID;
    private String fname;
    private String lname;
    private String email;
    private Date hireDate;
    private int salary;
    private String ssn;
    private String street;
    private String zip;
    private String gender;
    private String race;
    private Date dob;
    private String phoneNumber;
    private String stateName;
    private String stateCode;
    private int stateID;
    private String cityName;
    private int cityID;
    private String jobTitle;
    private String divisionName;

    public Employee(ResultSet rs) throws SQLException {
        ID = rs.getInt("empid");
        fname = rs.getString("Fname");
        lname = rs.getString("Lname");
        email = rs.getString("email");
        hireDate = rs.getDate("HireDate");
        salary = rs.getInt("Salary");
        ssn = rs.getString("SSN");
        street = rs.getString("street");
        zip = rs.getString("zip");
        gender = rs.getString("gender");
        race = rs.getString("identified_race");
        dob = rs.getDate("DOB");
        phoneNumber = rs.getString("phone_number");
        stateName = rs.getString("state_name");
        stateCode = rs.getString("state_code");
        stateID = rs.getInt("state_id");
        cityName = rs.getString("city_name");
        cityID = rs.getInt("city_id");
        jobTitle = rs.getString("job_title");
        divisionName = rs.getString("Name");
    }

    public int getID() {
        return ID;
    }

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	public String getEmail() {
		return email;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public int getSalary() {
		return salary;
	}

	public String getSsn() {
		return ssn;
	}

	public String getStreet() {
		return street;
	}

	public String getZip() {
		return zip;
	}

	public String getGender() {
		return gender;
	}

	public String getRace() {
		return race;
	}

	public Date getDob() {
		return dob;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getStateName() {
		return stateName;
	}

    public String getStateCode() {
        return stateCode;
    }

    public int getStateID() {
        return stateID;
    }

	public String getCityName() {
		return cityName;
	}

	public int getCityID() {
		return cityID;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getDivisionName() {
		return divisionName;
	}
}
