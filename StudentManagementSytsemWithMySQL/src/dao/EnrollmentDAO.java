package dao;


import config.DatabaseConfig;
import model.Enrollment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public boolean enrollStudent(Enrollment enrollment) {

        String sql = """
                INSERT INTO enrollments
                (student_id, course_id, enrollment_date, grade)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, enrollment.getStudentId());
            preparedStatement.setInt(2, enrollment.getCourseId());
            preparedStatement.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
            preparedStatement.setString(4, enrollment.getGrade());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<Enrollment> getAllEnrollments() {

        List<Enrollment> enrollments = new ArrayList<>();

        String sql = "SELECT * FROM enrollments ORDER BY enrollment_date";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                enrollments.add(mapEnrollment(resultSet));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return enrollments;
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) {

        List<Enrollment> enrollments = new ArrayList<>();

        String sql = """
                SELECT * FROM enrollments
                WHERE student_id = ?
                ORDER BY enrollment_date
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    enrollments.add(mapEnrollment(resultSet));
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return enrollments;
    }

    public List<Enrollment> getEnrollmentsByCourse(int courseId) {

        List<Enrollment> enrollments = new ArrayList<>();

        String sql = """
                SELECT * FROM enrollments
                WHERE course_id = ?
                ORDER BY enrollment_date
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    enrollments.add(mapEnrollment(resultSet));
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return enrollments;
    }

    public boolean updateGrade(int studentId, int courseId, String grade) {

        String sql = """
                UPDATE enrollments
                SET grade = ?
                WHERE student_id = ?
                  AND course_id = ?
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, grade);
            preparedStatement.setInt(2, studentId);
            preparedStatement.setInt(3, courseId);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteEnrollment(int studentId, int courseId) {

        String sql = """
                DELETE FROM enrollments
                WHERE student_id = ?
                  AND course_id = ?
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean enrollStudentsBatch(List<Enrollment> enrollments) {

        String sql = """
                INSERT INTO enrollments
                (student_id, course_id, enrollment_date, grade)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            for (Enrollment enrollment : enrollments) {

                preparedStatement.setInt(1, enrollment.getStudentId());
                preparedStatement.setInt(2, enrollment.getCourseId());
                preparedStatement.setDate(
                        3,
                        Date.valueOf(enrollment.getEnrollmentDate())
                );
                preparedStatement.setString(4, enrollment.getGrade());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private Enrollment mapEnrollment(ResultSet resultSet) throws SQLException {

        return new Enrollment(
                resultSet.getInt("student_id"),
                resultSet.getInt("course_id"),
                resultSet.getDate("enrollment_date").toLocalDate(),
                resultSet.getString("grade")
        );
    }
}
