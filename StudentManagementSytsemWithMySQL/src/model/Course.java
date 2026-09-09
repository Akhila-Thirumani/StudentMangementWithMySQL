package model;


public class Course {

    private int id;
    private String courseCode;
    private String courseName;
    private int credits;
    private String department;

    public Course(int id, String courseCode, String courseName,
                  int credits, String department) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.department = department;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

    // Generate getters and setters in Eclipse
}