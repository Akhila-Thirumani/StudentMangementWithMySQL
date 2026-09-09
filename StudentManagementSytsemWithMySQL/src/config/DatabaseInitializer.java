package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {

        String createStudentsTable = """
                CREATE TABLE IF NOT EXISTS students (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    student_id VARCHAR(20) NOT NULL UNIQUE,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    date_of_birth DATE,
                    email VARCHAR(100) UNIQUE,
                    phone VARCHAR(15),
                    department VARCHAR(100),
                    enrollment_date DATE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP
                )
                """;

        String createCoursesTable = """
                CREATE TABLE IF NOT EXISTS courses (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    course_code VARCHAR(20) NOT NULL UNIQUE,
                    course_name VARCHAR(100) NOT NULL,
                    credits INT NOT NULL,
                    department VARCHAR(100)
                )
                """;

        String createEnrollmentsTable = """
                CREATE TABLE IF NOT EXISTS enrollments (
                    student_id INT NOT NULL,
                    course_id INT NOT NULL,
                    enrollment_date DATE NOT NULL,
                    grade VARCHAR(5),

                    PRIMARY KEY (student_id, course_id),

                    FOREIGN KEY (student_id)
                        REFERENCES students(id)
                        ON DELETE CASCADE,

                    FOREIGN KEY (course_id)
                        REFERENCES courses(id)
                        ON DELETE CASCADE
                )
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createStudentsTable);
            statement.executeUpdate(createCoursesTable);
            statement.executeUpdate(createEnrollmentsTable);

            System.out.println("Database tables initialized successfully.");

        } catch (SQLException exception) {

            System.out.println("Database initialization failed.");
            exception.printStackTrace();
        }
    }

    public static void createIndexes() {

        String createDepartmentIndex = """
                CREATE INDEX idx_students_department
                ON students(department)
                """;

        String createLastNameIndex = """
                CREATE INDEX idx_students_last_name
                ON students(last_name)
                """;

        String createCourseIndex = """
                CREATE INDEX idx_enrollments_course_id
                ON enrollments(course_id)
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            createIndexIfNotExists(
                    connection,
                    statement,
                    "idx_students_department",
                    "students",
                    createDepartmentIndex
            );

            createIndexIfNotExists(
                    connection,
                    statement,
                    "idx_students_last_name",
                    "students",
                    createLastNameIndex
            );

            createIndexIfNotExists(
                    connection,
                    statement,
                    "idx_enrollments_course_id",
                    "enrollments",
                    createCourseIndex
            );

            System.out.println("Database indexes initialized successfully.");

        } catch (SQLException exception) {

            System.out.println("Index creation failed.");
            exception.printStackTrace();
        }
    }

    private static void createIndexIfNotExists(
            Connection connection,
            Statement statement,
            String indexName,
            String tableName,
            String createIndexSql) throws SQLException {

        String checkIndexSql = """
                SELECT COUNT(*)
                FROM information_schema.statistics
                WHERE table_schema = DATABASE()
                  AND table_name = ?
                  AND index_name = ?
                """;

        try (var preparedStatement =
                     connection.prepareStatement(checkIndexSql)) {

            preparedStatement.setString(1, tableName);
            preparedStatement.setString(2, indexName);

            try (var resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    statement.executeUpdate(createIndexSql);
                }
            }
        }
    }

    public static void createStoredProcedure() {

        String dropProcedure = """
                DROP PROCEDURE IF EXISTS GetStudentsByDepartment
                """;

        String createProcedure = """
                CREATE PROCEDURE GetStudentsByDepartment(
                    IN department_name VARCHAR(100)
                )
                BEGIN
                    SELECT
                        id,
                        student_id,
                        first_name,
                        last_name,
                        date_of_birth,
                        email,
                        phone,
                        department,
                        enrollment_date
                    FROM students
                    WHERE department = department_name
                    ORDER BY first_name, last_name;
                END
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(dropProcedure);
            statement.executeUpdate(createProcedure);

            System.out.println("Stored procedure created successfully.");

        } catch (SQLException exception) {

            System.out.println("Stored procedure creation failed.");
            exception.printStackTrace();
        }
    }
}