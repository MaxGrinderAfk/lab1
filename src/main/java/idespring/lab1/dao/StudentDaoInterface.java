package idespring.lab1.dao;

import java.util.*;
import idespring.lab1.model.Student;

public interface StudentDaoInterface {
    List<Student> findAll();

    List<Student> findByAgeAndSortByName(long age, String sort);

    List<Student> findByAge(int age);

    Student findById(long id);

    List<Student> sortByName(String sort);

    Student add(Student student);

    boolean update(Student student, long id);

    boolean delete(long id);
}
