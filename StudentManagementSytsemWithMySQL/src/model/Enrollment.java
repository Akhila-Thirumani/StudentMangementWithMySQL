package model;



import java.time.LocalDate;

public class Enrollment {

    private int studentId;
    private int courseId;
    private LocalDate enrollmentDate;
    private String grade;

    public Enrollment(int studentId, int courseId,
                      LocalDate enrollmentDate, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.grade = grade;
    }

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

    // Generate getters and setters in Eclipse
}