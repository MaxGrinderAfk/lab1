package idespring.lab1.dao;

import java.util.*;
import java.util.stream.Collectors;
import idespring.lab1.model.Student;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao implements StudentDaoInterface {
    private final Map<Long, Student> students = new HashMap<>();
    private long nextId = 1L;

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public List<Student> sortByName(String sort) {
        return students.values().stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findByAgeAndSortByName(long age, String sort) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Student findById(long id) {
        return students.get(id);
    }

    @Override
    public Student add(Student student) {
        student.setStudentId(nextId);
        students.put(nextId, student);
        nextId++;

        return student;
    }

    @Override
    public boolean update(Student student, long id) {
        if (!students.containsKey(id)) {
            return false;
        }

        students.get(id).setAge(student.getAge());
        students.get(id).setName(student.getName());
        students.get(id).setMarks(student.getMarks());

        return true;
    }

    @Override
    public boolean delete(long id) {
        if (!students.containsKey(id)) {
            return false;
        }

        students.remove(id);

        return true;
    }
}
