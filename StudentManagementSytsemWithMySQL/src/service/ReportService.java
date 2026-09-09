package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportService {

    public void displayDatabaseStatistics() {

        String studentCountSql = "SELECT COUNT(*) FROM students";
        String courseCountSql = "SELECT COUNT(*) FROM courses";
        String enrollmentCountSql = "SELECT COUNT(*) FROM enrollments";

        try (Connection connection = DatabaseConfig.getConnection()) {

            int studentCount = getCount(connection, studentCountSql);
            int courseCount = getCount(connection, courseCountSql);
            int enrollmentCount = getCount(connection, enrollmentCountSql);

            System.out.println("\n===== DATABASE STATISTICS =====");
            System.out.println("Total Students    : " + studentCount);
            System.out.println("Total Courses     : " + courseCount);
            System.out.println("Total Enrollments : " + enrollmentCount);

        } catch (SQLException exception) {
            System.out.println("Unable to retrieve database statistics.");
            exception.printStackTrace();
        }
    }

    public void displayStudentsByDepartment() {

        String sql = """
                SELECT department, COUNT(*) AS student_count
                FROM students
                GROUP BY department
                ORDER BY department
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\n===== STUDENTS BY DEPARTMENT =====");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString("department")
                        + " : "
                        + resultSet.getInt("student_count")
                );
            }

        } catch (SQLException exception) {
            System.out.println("Unable to generate department report.");
            exception.printStackTrace();
        }
    }

    private int getCount(Connection connection, String sql)
            throws SQLException {

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }
}