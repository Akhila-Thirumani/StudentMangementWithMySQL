import config.DatabaseInitializer;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.StudentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import service.ProcedureService;
import service.ReportService;
import util.DatabaseUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    private static final ReportService reportService = new ReportService();
    private static final ProcedureService procedureService = new ProcedureService();

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       STUDENT MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        DatabaseInitializer.initializeDatabase();
        DatabaseInitializer.createIndexes();
        DatabaseInitializer.createStoredProcedure();

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    viewAllStudents();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    updateStudent();
                    break;

                case 5:
                    deleteStudent();
                    break;

                case 6:
                    addCourse();
                    break;

                case 7:
                    viewAllCourses();
                    break;

                case 8:
                    deleteCourse();
                    break;

                case 9:
                    enrollStudent();
                    break;

                case 10:
                    viewEnrollments();
                    break;

                case 11:
                    updateGrade();
                    break;

                case 12:
                    deleteEnrollment();
                    break;

                case 13:
                    studentsByDepartment();
                    break;

                case 14:
                    displayDatabaseStatistics();
                    break;

                case 15:
                    backupDatabase();
                    break;

                case 16:
                    recoverDatabase();
                    break;

                case 17:
                    running = false;
                    System.out.println("Thank you for using Student Management System.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // ========================= MENU =========================

    private static void displayMenu() {

        System.out.println("\n==============================================");
        System.out.println("              MAIN MENU");
        System.out.println("==============================================");

        System.out.println("\n--- Student Management ---");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");

        System.out.println("\n--- Course Management ---");
        System.out.println("6. Add Course");
        System.out.println("7. View All Courses");
        System.out.println("8. Delete Course");

        System.out.println("\n--- Enrollment Management ---");
        System.out.println("9. Enroll Student");
        System.out.println("10. View Enrollments");
        System.out.println("11. Update Grade");
        System.out.println("12. Delete Enrollment");

        System.out.println("\n--- Reports & Database ---");
        System.out.println("13. Students By Department");
        System.out.println("14. Database Statistics");
        System.out.println("15. Backup Database");
        System.out.println("16. Recover Database");

        System.out.println("\n17. Exit");
        System.out.println("==============================================");
    }

    // ========================= STUDENT =========================

    private static void addStudent() {

        System.out.println("\n===== ADD STUDENT =====");

        String studentId = readString("Student ID: ");
        String firstName = readString("First Name: ");
        String lastName = readString("Last Name: ");

        String dateOfBirthInput =
                readString("Date of Birth (YYYY-MM-DD): ");

        LocalDate dateOfBirth = null;

        if (!dateOfBirthInput.isBlank()) {
            try {
                dateOfBirth = LocalDate.parse(dateOfBirthInput);
            } catch (Exception exception) {
                System.out.println("Invalid date format.");
                return;
            }
        }

        String email = readString("Email: ");
        String phone = readString("Phone: ");
        String department = readString("Department: ");

        String enrollmentDateInput =
                readString("Enrollment Date (YYYY-MM-DD): ");

        LocalDate enrollmentDate;

        try {
            enrollmentDate = LocalDate.parse(enrollmentDateInput);
        } catch (Exception exception) {
            System.out.println("Invalid enrollment date.");
            return;
        }

        Student student = new Student(
                0,
                studentId,
                firstName,
                lastName,
                dateOfBirth,
                email,
                phone,
                department,
                enrollmentDate
        );

        boolean added = studentDAO.addStudent(student);

        if (added) {
            System.out.println("Student added successfully.");
        } else {
            System.out.println("Failed to add student.");
        }
    }

    private static void viewAllStudents() {

        System.out.println("\n===== ALL STUDENTS =====");

        List<Student> students = studentDAO.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students) {
            displayStudent(student);
        }
    }

    private static void searchStudent() {

        System.out.println("\n===== SEARCH STUDENT =====");

        String keyword =
                readString("Enter student ID, name, email or department: ");

        List<Student> students =
                studentDAO.searchStudents(keyword);

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students) {
            displayStudent(student);
        }
    }

    private static void updateStudent() {

        System.out.println("\n===== UPDATE STUDENT =====");

        int id = readInt("Enter database student ID: ");

        Student student = studentDAO.getStudentById(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Enter new details.");

        String firstName = readString("First Name: ");
        String lastName = readString("Last Name: ");

        String dateOfBirthInput =
                readString("Date of Birth (YYYY-MM-DD): ");

        LocalDate dateOfBirth = null;

        if (!dateOfBirthInput.isBlank()) {
            try {
                dateOfBirth = LocalDate.parse(dateOfBirthInput);
            } catch (Exception exception) {
                System.out.println("Invalid date format.");
                return;
            }
        }

        String email = readString("Email: ");
        String phone = readString("Phone: ");
        String department = readString("Department: ");

        String enrollmentDateInput =
                readString("Enrollment Date (YYYY-MM-DD): ");

        LocalDate enrollmentDate;

        try {
            enrollmentDate = LocalDate.parse(enrollmentDateInput);
        } catch (Exception exception) {
            System.out.println("Invalid enrollment date.");
            return;
        }

        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setDateOfBirth(dateOfBirth);
        student.setEmail(email);
        student.setPhone(phone);
        student.setDepartment(department);
        student.setEnrollmentDate(enrollmentDate);

        boolean updated = studentDAO.updateStudent(student);

        if (updated) {
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Failed to update student.");
        }
    }

    private static void deleteStudent() {

        System.out.println("\n===== DELETE STUDENT =====");

        int id = readInt("Enter student database ID: ");

        Student student = studentDAO.getStudentById(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println(
                "Student: "
                        + student.getFirstName()
                        + " "
                        + student.getLastName()
        );

        String confirmation =
                readString("Delete this student? (yes/no): ");

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean deleted = studentDAO.deleteStudent(id);

        if (deleted) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Failed to delete student.");
        }
    }

    private static void displayStudent(Student student) {

        System.out.println("----------------------------------------------");
        System.out.println("Database ID   : " + student.getId());
        System.out.println("Student ID    : " + student.getStudentId());
        System.out.println("Name          : "
                + student.getFirstName()
                + " "
                + student.getLastName());
        System.out.println("Date of Birth : " + student.getDateOfBirth());
        System.out.println("Email         : " + student.getEmail());
        System.out.println("Phone         : " + student.getPhone());
        System.out.println("Department    : " + student.getDepartment());
        System.out.println("Enrollment    : " + student.getEnrollmentDate());
    }

    // ========================= COURSE =========================

    private static void addCourse() {

        System.out.println("\n===== ADD COURSE =====");

        String courseCode = readString("Course Code: ");
        String courseName = readString("Course Name: ");
        int credits = readInt("Credits: ");
        String department = readString("Department: ");

        Course course = new Course(
                0,
                courseCode,
                courseName,
                credits,
                department
        );

        boolean added = courseDAO.addCourse(course);

        if (added) {
            System.out.println("Course added successfully.");
        } else {
            System.out.println("Failed to add course.");
        }
    }

    private static void viewAllCourses() {

        System.out.println("\n===== ALL COURSES =====");

        List<Course> courses = courseDAO.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        for (Course course : courses) {

            System.out.println("----------------------------------------------");
            System.out.println("ID         : " + course.getId());
            System.out.println("Code       : " + course.getCourseCode());
            System.out.println("Name       : " + course.getCourseName());
            System.out.println("Credits    : " + course.getCredits());
            System.out.println("Department : " + course.getDepartment());
        }
    }

    private static void deleteCourse() {

        System.out.println("\n===== DELETE COURSE =====");

        int id = readInt("Enter course ID: ");

        Course course = courseDAO.getCourseById(id);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.println(
                "Course: "
                        + course.getCourseCode()
                        + " - "
                        + course.getCourseName()
        );

        String confirmation =
                readString("Delete this course? (yes/no): ");

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean deleted = courseDAO.deleteCourse(id);

        if (deleted) {
            System.out.println("Course deleted successfully.");
        } else {
            System.out.println("Failed to delete course.");
        }
    }

    // ========================= ENROLLMENT =========================

    private static void enrollStudent() {

        System.out.println("\n===== ENROLL STUDENT =====");

        int studentId = readInt("Student database ID: ");
        int courseId = readInt("Course ID: ");

        String enrollmentDateInput =
                readString("Enrollment Date (YYYY-MM-DD): ");

        LocalDate enrollmentDate;

        try {
            enrollmentDate = LocalDate.parse(enrollmentDateInput);
        } catch (Exception exception) {
            System.out.println("Invalid enrollment date.");
            return;
        }

        String grade = readString("Grade: ");

        Enrollment enrollment = new Enrollment(
                studentId,
                courseId,
                enrollmentDate,
                grade
        );

        boolean enrolled =
                enrollmentDAO.enrollStudent(enrollment);

        if (enrolled) {
            System.out.println("Student enrolled successfully.");
        } else {
            System.out.println("Failed to enroll student.");
        }
    }

    private static void viewEnrollments() {

        System.out.println("\n===== VIEW ENROLLMENTS =====");

        System.out.println("1. View All Enrollments");
        System.out.println("2. View By Student");
        System.out.println("3. View By Course");

        int choice = readInt("Enter choice: ");

        List<Enrollment> enrollments;

        switch (choice) {

            case 1:
                enrollments =
                        enrollmentDAO.getAllEnrollments();
                break;

            case 2:
                int studentId =
                        readInt("Enter student database ID: ");

                enrollments =
                        enrollmentDAO.getEnrollmentsByStudent(studentId);
                break;

            case 3:
                int courseId =
                        readInt("Enter course ID: ");

                enrollments =
                        enrollmentDAO.getEnrollmentsByCourse(courseId);
                break;

            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }

        for (Enrollment enrollment : enrollments) {

            System.out.println("----------------------------------------------");
            System.out.println("Student ID      : "
                    + enrollment.getStudentId());
            System.out.println("Course ID       : "
                    + enrollment.getCourseId());
            System.out.println("Enrollment Date : "
                    + enrollment.getEnrollmentDate());
            System.out.println("Grade           : "
                    + enrollment.getGrade());
        }
    }

    private static void updateGrade() {

        System.out.println("\n===== UPDATE GRADE =====");

        int studentId =
                readInt("Enter student database ID: ");

        int courseId =
                readInt("Enter course ID: ");

        String grade =
                readString("Enter new grade: ");

        boolean updated =
                enrollmentDAO.updateGrade(
                        studentId,
                        courseId,
                        grade
                );

        if (updated) {
            System.out.println("Grade updated successfully.");
        } else {
            System.out.println("Failed to update grade.");
        }
    }

    private static void deleteEnrollment() {

        System.out.println("\n===== DELETE ENROLLMENT =====");

        int studentId =
                readInt("Enter student database ID: ");

        int courseId =
                readInt("Enter course ID: ");

        String confirmation =
                readString("Delete this enrollment? (yes/no): ");

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean deleted =
                enrollmentDAO.deleteEnrollment(
                        studentId,
                        courseId
                );

        if (deleted) {
            System.out.println("Enrollment deleted successfully.");
        } else {
            System.out.println("Failed to delete enrollment.");
        }
    }

    // ========================= REPORTS =========================

    private static void studentsByDepartment() {

        System.out.println("\n===== STUDENTS BY DEPARTMENT =====");

        String department =
                readString("Enter department: ");

        procedureService.displayStudentsByDepartment(department);
    }

    private static void displayDatabaseStatistics() {

        reportService.displayDatabaseStatistics();
        reportService.displayStudentsByDepartment();
    }

    // ========================= BACKUP =========================

    private static void backupDatabase() {

        System.out.println("\n===== DATABASE BACKUP =====");

        String filePath = readString(
                "Enter backup file name "
                        + "(example: student_backup.sql): "
        );

        if (filePath.isBlank()) {
            System.out.println("Backup file name cannot be empty.");
            return;
        }

        boolean backupSuccessful =
                DatabaseUtils.backupDatabase(filePath);

        if (backupSuccessful) {
            System.out.println("Backup completed successfully.");
        } else {
            System.out.println("Backup failed.");
        }
    }

    // ========================= RECOVERY =========================

    private static void recoverDatabase() {

        System.out.println("\n===== DATABASE RECOVERY =====");

        String filePath =
                readString("Enter backup file path: ");

        if (filePath.isBlank()) {
            System.out.println("Backup file path cannot be empty.");
            return;
        }

        System.out.println(
                "WARNING: Recovery will execute the SQL statements "
                        + "from the backup file."
        );

        String confirmation =
                readString("Continue? (yes/no): ");

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Recovery cancelled.");
            return;
        }

        boolean recoverySuccessful =
                DatabaseUtils.recoverDatabase(filePath);

        if (recoverySuccessful) {
            System.out.println("Recovery completed successfully.");
        } else {
            System.out.println("Recovery failed.");
        }
    }

    // ========================= INPUT UTILITIES =========================

    private static String readString(String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException exception) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }
}