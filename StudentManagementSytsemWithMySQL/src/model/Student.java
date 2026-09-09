package model;


import java.time.LocalDate;

public class Student {

    private int id;
    private String studentId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String department;
    private LocalDate enrollmentDate;
    
    public Student(int id, String studentId, String firstName, String lastName,
            LocalDate dateOfBirth, String email, String phone,
            String department, LocalDate enrollmentDate) {

 this.id = id;
 this.studentId = studentId;
 this.firstName = firstName;
 this.lastName = lastName;
 this.dateOfBirth = dateOfBirth;
 this.email = email;
 this.phone = phone;
 this.department = department;
 this.enrollmentDate = enrollmentDate;
}
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getFirstName() {
    	return firstName;
    	
    }
    public void setFirstname(String firstName) {
    	this.firstName=firstName;
    }
    public String getLastName() {
    	return lastName;
    	
    }
    public void setLastName(String lastName) {
    	this.lastName=lastName;
    }
    public LocalDate getDateOfBirth() {
    	return dateOfBirth;
    	
    }
    public void setDateOfBirth() {
    	this.dateOfBirth=dateOfBirth;
    }
    public String getEmail() {
    	return email;
    }
    public void setEmail(String email) {
    	this.email=email;
    }
    public String getPhone() {
    	return phone;
}
    public void setPhone(String phone) {
    	this.phone=phone;
    }
    public String getDepartment() {
    	return department;
    }
	public void setdepartment() {
		this.department=department;
	}

	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}