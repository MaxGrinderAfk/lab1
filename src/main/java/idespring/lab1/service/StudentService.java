package idespring.lab1.service;

import idespring.lab1.dao.StudentDaoInterface;
import java.util.*;
import idespring.lab1.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService implements StudentServInterface {
    private final StudentDaoInterface studDao;

    @Autowired
    public StudentService(StudentDaoInterface studDao) {
        this.studDao = studDao;
    }

    @Override
    public List<Student> readStudents(Integer age, String sort, Long id) {
        if (id != null) {
            List<Student> students = new ArrayList<>();
            students.add(studDao.findById(id));

            return students;
        }

        List<Student> students;

        if (age != null && sort != null) {
            students = studDao.findByAgeAndSortByName(age, sort);
        } else if (age != null) {
            students = studDao.findByAge(age);
        } else if (sort != null) {
            students = studDao.sortByName(sort);
        } else {
            students = studDao.findAll();
        }
        return students;
    }

    @Override
    public Student addStudent(Student student) {
        return studDao.add(student);
    }

    @Override
    public boolean updateStudent(Student student, long id) {
        return studDao.update(student, id);
    }

    @Override
    public boolean deleteStudent(long id) {
        return studDao.delete(id);
    }
}
