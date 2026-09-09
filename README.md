# StudentMangementWithMySQL

## Project Overview

The Student Management System is a Java-based database application developed using **JDBC and MySQL**.

The application allows users to manage students, courses, and enrollments through a menu-driven interface. It demonstrates database programming concepts such as CRUD operations, PreparedStatement, connection pooling, transactions, batch processing, stored procedures, and database backup and recovery.

### Objectives

- Manage student information efficiently.
- Manage course information.
- Enroll students into courses.
- Perform CRUD operations using JDBC.
- Demonstrate secure database operations using PreparedStatement.
- Implement database connection pooling.
- Implement transaction management and batch processing.
- Use stored procedures for database operations.
- Provide database backup and recovery utilities.

---

## Technologies Used

- Java
- JDBC
- MySQL
- Apache Commons DBCP2
- Apache Commons Pool2
- Apache Commons Logging
- Eclipse IDE
- MySQL Workbench
- Git & GitHub

---

## Features

### Student Management
- Add student
- View all students
- Search student
- Update student
- Delete student

### Course Management
- Add course
- View all courses
- Delete course

### Enrollment Management
- Enroll student in a course
- View enrollments
- Update student grade
- Delete enrollment
- Batch enrollment operations

### Database Features
- JDBC database connectivity
- Connection pooling
- PreparedStatement
- Transaction management
- Batch processing
- Stored procedures
- Database indexes
- Database statistics
- Backup and recovery

---

# Project Structure

```text
StudentManagementSystem/
│
├── src/
│   ├── config/
│   │   ├── DatabaseConfig.java
│   │   └── DatabaseInitializer.java
│   │
│   ├── model/
│   │   ├── Student.java
│   │   ├── Course.java
│   │   └── Enrollment.java
│   │
│   ├── dao/
│   │   ├── StudentDAO.java
│   │   ├── CourseDAO.java
│   │   └── EnrollmentDAO.java
│   │
│   ├── service/
│   │   ├── StudentService.java
│   │   ├── EnrollmentService.java
│   │   ├── ReportService.java
│   │   └── ProcedureService.java
│   │
│   ├── util/
│   │   └── DatabaseUtils.java
│   │
│   └── Main.java
│
├── database/
│   └── library_schema.sql
│
├── docs/
│
├── tests/
│
├── config/
│
├── lib/
│
└── README.md
