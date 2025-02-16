package idespring.lab1.serviceTest;

import idespring.lab1.dao.StudentDaoInterface;
import idespring.lab1.model.Student;
import idespring.lab1.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTestTest {
    @Mock
    private StudentDaoInterface studDao;

    @InjectMocks
    private StudentService studentService;

    List<Integer> marks = new ArrayList<>(Arrays.asList(3, 4, 5));
    Student stud = new Student("Bob", 25, 13, marks);
    Student stud2 = new Student("Mark", 22, 11, marks);

    public List<Student> addStudentsToList()
    {
        List<Student> students = new ArrayList<>(2);

        students.add(stud);
        students.add(stud2);

        return students;
    }

    @Test
    public void shouldReadStudentsById() {
        when(studDao.findById(13L)).thenReturn(stud);
        List<Student> result = studentService.readStudents(null, null, 13L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bob", result.getFirst().getName());
    }

    @Test
    public void shouldReadAllStudents() {
        List<Student> students = this.addStudentsToList();

        when(studDao.findAll()).thenReturn(students);
        List<Student> result = studentService.readStudents(null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bob", result.getFirst().getName());
    }

    @Test
    public void shouldReadStudentsByAge() {
        List<Student> students = new ArrayList<>();
        students.add(stud2);

        when(studDao.findByAge(22)).thenReturn(students);
        List<Student> result = studentService.readStudents(22, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mark", result.getFirst().getName());
    }

    @Test
    public void shouldReadStudentsAndSortByName() {
        List<Student> students = this.addStudentsToList();

        when(studDao.sortByName("sort")).thenReturn(students);
        List<Student> result = studentService.readStudents(null, "sort", null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bob", result.getFirst().getName());
    }

    @Test
    public void shouldReadStudentsByAgeAndSortByName() {
        List<Student> students = new ArrayList<>();
        students.add(stud);

        when(studDao.findByAgeAndSortByName(25, "sort")).thenReturn(students);
        List<Student> result = studentService.readStudents(25, "sort", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bob", result.getFirst().getName());
    }

    @Test
    public void shouldAddStudent() {
        when(studDao.add(stud)).thenReturn(stud);

        Student result = studentService.addStudent(stud);
        assertNotNull(result);
        assertEquals("Bob", result.getName());
    }

    @Test
    public void shouldUpdateStudent() {
        Student studN = new Student("NS", 30, 30, marks);

        when(studDao.update(studN, 13)).thenReturn(true);

        Boolean result = studentService.updateStudent(studN, 13);
        assertNotNull(result);
    }

    @Test
    public void shouldDeleteStudent() {
        when(studDao.delete(13)).thenReturn(true);

        Boolean result = studentService.deleteStudent(13);
        assertNotNull(result);
    }
}
