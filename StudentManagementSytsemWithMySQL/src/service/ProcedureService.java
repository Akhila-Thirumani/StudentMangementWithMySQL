package service;

import config.DatabaseConfig;
import model.Student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProcedureService {

    public List<Student> getStudentsByDepartment(String department) {

        List<Student> students = new ArrayList<>();

        String sql = "{CALL GetStudentsByDepartment(?)}";

        try (Connection connection = DatabaseConfig.getConnection();
             CallableStatement callableStatement =
                     connection.prepareCall(sql)) {

            callableStatement.setString(1, department);

            try (ResultSet resultSet = callableStatement.executeQuery()) {

                while (resultSet.next()) {

                    Student student = new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("student_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("date_of_birth") != null
                                    ? resultSet.getDate("date_of_birth").toLocalDate()
                                    : null,
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getString("department"),
                            resultSet.getDate("enrollment_date").toLocalDate()
                    );

                    students.add(student);
                }
            }

        } catch (SQLException exception) {

            System.out.println("Unable to execute stored procedure.");
            exception.printStackTrace();
        }

        return students;
    }

    public void displayStudentsByDepartment(String department) {

        List<Student> students = getStudentsByDepartment(department);

        System.out.println("\n===== STUDENTS IN " + department.toUpperCase() + " =====");

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students) {

            System.out.println(
                    "ID: " + student.getId()
                    + " | Student ID: " + student.getStudentId()
                    + " | Name: " + student.getFirstName()
                    + " " + student.getLastName()
                    + " | Email: " + student.getEmail()
                    + " | Department: " + student.getDepartment()
            );
        }
    }
}