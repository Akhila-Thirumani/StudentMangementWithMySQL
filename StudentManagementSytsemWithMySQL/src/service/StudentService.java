package service;



import dao.StudentDAO;
import model.Student;

import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public boolean addStudent(Student student) {
        return studentDAO.addStudent(student);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public Student getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    public Student getStudentByStudentId(String studentId) {
        return studentDAO.getStudentByStudentId(studentId);
    }

    public List<Student> searchStudents(String keyword) {
        return studentDAO.searchStudents(keyword);
    }

    public boolean updateStudent(Student student) {
        return studentDAO.updateStudent(student);
    }

    public boolean deleteStudent(int id) {
        return studentDAO.deleteStudent(id);
    }
}