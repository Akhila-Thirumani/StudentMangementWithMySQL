package util;

import config.DatabaseConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseUtils;

public class DatabaseUtils {

    private static final String DATABASE_NAME = "student_management_db";

    public static boolean backupDatabase(String filePath) {

        File backupFile = new File(filePath);

        try (Connection connection = DatabaseConfig.getConnection();
             BufferedWriter writer = new BufferedWriter(
                     new FileWriter(backupFile))) {

            writer.write("-- Student Management System Database Backup");
            writer.newLine();
            writer.write("-- Database: " + DATABASE_NAME);
            writer.newLine();
            writer.newLine();

            backupTable(connection, writer, "students");
            backupTable(connection, writer, "courses");
            backupTable(connection, writer, "enrollments");

            System.out.println(
                    "Database backup created successfully: "
                            + backupFile.getAbsolutePath()
            );

            return true;

        } catch (SQLException | IOException exception) {

            System.out.println("Database backup failed.");
            exception.printStackTrace();

            return false;
        }
    }

    private static void backupTable(
            Connection connection,
            BufferedWriter writer,
            String tableName)
            throws SQLException, IOException {

        writer.write("-- Backup for table: " + tableName);
        writer.newLine();

        writeCreateTableStatement(connection, writer, tableName);
        writeInsertStatements(connection, writer, tableName);

        writer.newLine();
    }

    private static void writeCreateTableStatement(
            Connection connection,
            BufferedWriter writer,
            String tableName)
            throws SQLException, IOException {

        String query = """
                SELECT COLUMN_NAME,
                       COLUMN_TYPE,
                       IS_NULLABLE,
                       COLUMN_KEY,
                       EXTRA
                FROM information_schema.columns
                WHERE table_schema = ?
                  AND table_name = ?
                ORDER BY ORDINAL_POSITION
                """;

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setString(1, DATABASE_NAME);
            preparedStatement.setString(2, tableName);

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                writer.write("CREATE TABLE IF NOT EXISTS "
                        + tableName + " (");
                writer.newLine();

                List<String> columns = new ArrayList<>();

                while (resultSet.next()) {

                    String columnName =
                            resultSet.getString("COLUMN_NAME");

                    String columnType =
                            resultSet.getString("COLUMN_TYPE");

                    String nullable =
                            resultSet.getString("IS_NULLABLE");

                    String columnKey =
                            resultSet.getString("COLUMN_KEY");

                    String extra =
                            resultSet.getString("EXTRA");

                    StringBuilder columnDefinition =
                            new StringBuilder();

                    columnDefinition
                            .append("    ")
                            .append(columnName)
                            .append(" ")
                            .append(columnType);

                    if ("NO".equals(nullable)) {
                        columnDefinition.append(" NOT NULL");
                    }

                    if ("PRI".equals(columnKey)) {
                        columnDefinition.append(" PRIMARY KEY");
                    }

                    if (extra != null
                            && extra.contains("auto_increment")) {
                        columnDefinition.append(" AUTO_INCREMENT");
                    }

                    columns.add(columnDefinition.toString());
                }

                writer.write(String.join(",\n", columns));
                writer.newLine();
                writer.write(");");
                writer.newLine();
                writer.newLine();
            }
        }
    }

    private static void writeInsertStatements(
            Connection connection,
            BufferedWriter writer,
            String tableName)
            throws SQLException, IOException {

        String query = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            DatabaseMetaData metadata =
                    connection.getMetaData();

            int columnCount =
                    resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {

                StringBuilder insertStatement =
                        new StringBuilder();

                insertStatement.append("INSERT INTO ")
                        .append(tableName)
                        .append(" VALUES (");

                for (int column = 1;
                     column <= columnCount;
                     column++) {

                    Object value =
                            resultSet.getObject(column);

                    if (value == null) {

                        insertStatement.append("NULL");

                    } else if (value instanceof Number) {

                        insertStatement.append(value);

                    } else {

                        String escapedValue =
                                value.toString()
                                        .replace("'", "''");

                        insertStatement.append("'")
                                .append(escapedValue)
                                .append("'");
                    }

                    if (column < columnCount) {
                        insertStatement.append(", ");
                    }
                }

                insertStatement.append(");");

                writer.write(insertStatement.toString());
                writer.newLine();
            }
        }
    }

    public static boolean recoverDatabase(String filePath) {

        File backupFile = new File(filePath);

        if (!backupFile.exists()) {

            System.out.println(
                    "Backup file not found: "
                            + backupFile.getAbsolutePath()
            );

            return false;
        }

        try (Connection connection =
                     DatabaseConfig.getConnection()) {

            connection.setAutoCommit(false);

            try (java.io.BufferedReader reader =
                         new java.io.BufferedReader(
                                 new java.io.FileReader(backupFile))) {

                String line;

                try (Statement statement =
                             connection.createStatement()) {

                    while ((line = reader.readLine()) != null) {

                        line = line.trim();

                        if (line.isEmpty()
                                || line.startsWith("--")) {
                            continue;
                        }

                        StringBuilder sql =
                                new StringBuilder(line);

                        while (!line.endsWith(";")
                                && (line = reader.readLine()) != null) {

                            sql.append(" ")
                                    .append(line.trim());
                        }

                        String sqlStatement =
                                sql.toString();

                        if (sqlStatement.endsWith(";")) {
                            sqlStatement =
                                    sqlStatement.substring(
                                            0,
                                            sqlStatement.length() - 1
                                    );
                        }

                        statement.executeUpdate(sqlStatement);
                    }
                }

                connection.commit();

                System.out.println(
                        "Database recovered successfully."
                );

                return true;

            } catch (Exception exception) {

                connection.rollback();

                System.out.println(
                        "Database recovery failed. "
                                + "Changes were rolled back."
                );

                exception.printStackTrace();

                return false;

            } finally {

                connection.setAutoCommit(true);
            }

        } catch (SQLException exception) {

            System.out.println("Database recovery failed.");
            exception.printStackTrace();

            return false;
        }
    }
}