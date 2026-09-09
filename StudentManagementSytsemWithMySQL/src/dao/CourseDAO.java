package dao;



import config.DatabaseConfig;
import model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public boolean addCourse(Course course) {

        String sql = """
                INSERT INTO courses
                (course_code, course_name, credits, department)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, course.getCourseCode());
            preparedStatement.setString(2, course.getCourseName());
            preparedStatement.setInt(3, course.getCredits());
            preparedStatement.setString(4, course.getDepartment());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<Course> getAllCourses() {

        List<Course> courses = new ArrayList<>();

        String sql = "SELECT * FROM courses ORDER BY id";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                courses.add(mapCourse(resultSet));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return courses;
    }

    public Course getCourseById(int id) {

        String sql = "SELECT * FROM courses WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCourse(resultSet);
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public Course getCourseByCode(String courseCode) {

        String sql = "SELECT * FROM courses WHERE course_code = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, courseCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCourse(resultSet);
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public boolean updateCourse(Course course) {

        String sql = """
                UPDATE courses
                SET course_code = ?,
                    course_name = ?,
                    credits = ?,
                    department = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, course.getCourseCode());
            preparedStatement.setString(2, course.getCourseName());
            preparedStatement.setInt(3, course.getCredits());
            preparedStatement.setString(4, course.getDepartment());
            preparedStatement.setInt(5, course.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int id) {

        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean addCoursesBatch(List<Course> courses) {

        String sql = """
                INSERT INTO courses
                (course_code, course_name, credits, department)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            for (Course course : courses) {

                preparedStatement.setString(1, course.getCourseCode());
                preparedStatement.setString(2, course.getCourseName());
                preparedStatement.setInt(3, course.getCredits());
                preparedStatement.setString(4, course.getDepartment());

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

    private Course mapCourse(ResultSet resultSet) throws SQLException {

        return new Course(
                resultSet.getInt("id"),
                resultSet.getString("course_code"),
                resultSet.getString("course_name"),
                resultSet.getInt("credits"),
                resultSet.getString("department")
        );
    }
}