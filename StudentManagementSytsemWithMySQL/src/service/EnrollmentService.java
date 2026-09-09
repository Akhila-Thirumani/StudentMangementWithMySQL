package service;

import config.DatabaseConfig;
import model.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnrollmentService {

    public boolean enrollStudent(Enrollment enrollment) {

        String sql = """
                INSERT INTO enrollments
                (student_id, course_id, enrollment_date, grade)
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = null;

        try {
            connection = DatabaseConfig.getConnection();

            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, enrollment.getStudentId());
                preparedStatement.setInt(2, enrollment.getCourseId());
                preparedStatement.setDate(
                        3,
                        java.sql.Date.valueOf(enrollment.getEnrollmentDate())
                );
                preparedStatement.setString(4, enrollment.getGrade());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    connection.commit();
                    return true;
                }

                connection.rollback();
                return false;
            }

        } catch (SQLException exception) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            exception.printStackTrace();
            return false;

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }
}