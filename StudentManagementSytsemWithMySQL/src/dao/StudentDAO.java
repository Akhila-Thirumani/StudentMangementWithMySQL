package dao;



import config.DatabaseConfig;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean addStudent(Student student) {

        String sql = """
                INSERT INTO students
                (student_id, first_name, last_name, date_of_birth,
                 email, phone, department, enrollment_date)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, student.getStudentId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.setDate(4, Date.valueOf(student.getDateOfBirth()));
            preparedStatement.setString(5, student.getEmail());
            preparedStatement.setString(6, student.getPhone());
            preparedStatement.setString(7, student.getDepartment());
            preparedStatement.setDate(8, Date.valueOf(student.getEnrollmentDate()));

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapStudent(resultSet));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return students;
    }

    public Student getStudentById(int id) {

        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return mapStudent(resultSet);
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public Student getStudentByStudentId(String studentId) {

        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return mapStudent(resultSet);
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public List<Student> searchStudents(String keyword) {

        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT * FROM students
                WHERE first_name LIKE ?
                   OR last_name LIKE ?
                   OR student_id LIKE ?
                   OR department LIKE ?
                   OR email LIKE ?
                ORDER BY id
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";

            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            preparedStatement.setString(4, searchPattern);
            preparedStatement.setString(5, searchPattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    students.add(mapStudent(resultSet));
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return students;
    }

    public boolean updateStudent(Student student) {

        String sql = """
                UPDATE students
                SET student_id = ?,
                    first_name = ?,
                    last_name = ?,
                    date_of_birth = ?,
                    email = ?,
                    phone = ?,
                    department = ?,
                    enrollment_date = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, student.getStudentId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.setDate(4, Date.valueOf(student.getDateOfBirth()));
            preparedStatement.setString(5, student.getEmail());
            preparedStatement.setString(6, student.getPhone());
            preparedStatement.setString(7, student.getDepartment());
            preparedStatement.setDate(8, Date.valueOf(student.getEnrollmentDate()));
            preparedStatement.setInt(9, student.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int id) {

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private Student mapStudent(ResultSet resultSet) throws SQLException {

        return new Student(
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
    }
}