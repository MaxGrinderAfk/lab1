package idespring.lab1.service;

import java.util.List;
import idespring.lab1.model.Student;

public interface StudentServInterface {
    List<Student> readStudents(Integer age, String sort, Long id);

    Student addStudent(Student student);

    boolean updateStudent(Student student, long id);

    boolean deleteStudent(long id);
}
