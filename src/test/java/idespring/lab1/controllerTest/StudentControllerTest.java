package idespring.lab1.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import idespring.lab1.controller.StudentController;
import idespring.lab1.model.Student;
import idespring.lab1.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student testStudent;
    private List<Student> studentList;

    @BeforeEach
    void setup() {
        List<Integer> grades1 = new ArrayList<>(Arrays.asList(4, 5, 3, 5));

        testStudent = new Student();
        testStudent.setStudentId(1L);
        testStudent.setName("Test Student");
        testStudent.setAge(20);
        testStudent.setMarks(grades1);

        List<Integer> grades2 = new ArrayList<>(Arrays.asList(3, 4, 5, 4));

        Student student2 = new Student();
        student2.setStudentId(2L);
        student2.setName("Another Student");
        student2.setAge(25);
        student2.setMarks(grades2);

        studentList = new ArrayList<>(Arrays.asList(testStudent, student2));
    }

    @Test
    void createStudentShouldReturnCreatedStudent() throws Exception {
        when(studentService.addStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/students/addStud")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(testStudent.getStudentId()))
                .andExpect(jsonPath("$.name").value(testStudent.getName()))
                .andExpect(jsonPath("$.age").value(testStudent.getAge()))
                .andExpect(jsonPath("$.marks").isArray())
                .andExpect(jsonPath("$.marks", containsInAnyOrder(4, 5, 3, 5)));
        }

    @Test
    void getStudentByIdWhenStudentExistsShouldReturnStudent() throws Exception {

        List<Student> singleStudentList = new ArrayList<>();
        singleStudentList.add(testStudent);
        when(studentService.readStudents(null, null, 1L)).thenReturn(singleStudentList);

        mockMvc.perform(get("/students/get/{studentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(testStudent.getStudentId()))
                .andExpect(jsonPath("$[0].name").value(testStudent.getName()))
                .andExpect(jsonPath("$[0].age").value(testStudent.getAge()))
                .andExpect(jsonPath("$[0].marks").isArray())
                .andExpect(jsonPath("$[0].marks", containsInAnyOrder(4, 5, 3, 5)));
    }

    @Test
    void getStudentByIdWhenStudentDoesNotExistShouldReturnNotFound() throws Exception {
        when(studentService.readStudents(isNull(), isNull(), eq(999L))).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/students/get/{studentId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsWhenStudentsExistShouldReturnAllStudents() throws Exception {
        when(studentService.readStudents(isNull(), isNull(), isNull())).thenReturn(studentList);

        mockMvc.perform(get("/students/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].studentId").value(testStudent.getStudentId()))
                .andExpect(jsonPath("$[1].studentId").value(2L));
    }

    @Test
    void getStudentsWithAgeParamShouldReturnFilteredStudents() throws Exception {
        List<Student> filteredList = new ArrayList<>();
        filteredList.add(testStudent);
        when(studentService.readStudents(eq(20), isNull(), isNull())).thenReturn(filteredList);

        mockMvc.perform(get("/students/get")
                        .param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].age").value(20));
    }

    @Test
    void getStudentsWithSortParamShouldReturnSortedStudents() throws Exception {
        when(studentService.readStudents(isNull(), eq("asc"), isNull())).thenReturn(studentList);

        mockMvc.perform(get("/students/get")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getStudentsWhenNoStudentsFoundShouldReturnNotFound() throws Exception {
        when(studentService.readStudents(any(), any(), any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/students/get"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudentWhenSuccessfulShouldReturnUpdatedStudent() throws Exception {
        testStudent.setName("Updated Name");

        List<Student> updatedStudentList = new ArrayList<>();
        updatedStudentList.add(testStudent);
        when(studentService.updateStudent(any(Student.class), eq(1L))).thenReturn(true);
        when(studentService.readStudents(isNull(), isNull(), eq(1L))).thenReturn(updatedStudentList);

        mockMvc.perform(put("/students/update/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].studentId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Updated Name"));
    }

    @Test
    void updateStudentWhenUnsuccessfulShouldReturnNotModified() throws Exception {
        when(studentService.updateStudent(any(Student.class), eq(999L))).thenReturn(false);

        mockMvc.perform(put("/students/update/{studentId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isNotModified());
    }

    @Test
    void deleteStudentWhenSuccessfulShouldReturnRemainingStudents() throws Exception {
        when(studentService.deleteStudent(2L)).thenReturn(true);
        List<Student> remainingStudents = new ArrayList<>();
        remainingStudents.add(testStudent);
        when(studentService.readStudents(isNull(), isNull(), isNull())).thenReturn(remainingStudents);

        mockMvc.perform(delete("/students/delete/{studentId}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].studentId").value(1L));
    }

    @Test
    void deleteStudentWhenUnsuccessfulShouldReturnMethodNotAllowed() throws Exception {
        when(studentService.deleteStudent(999L)).thenReturn(false);

        mockMvc.perform(delete("/students/delete/{studentId}", 999L))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deleteStudentWhenSuccessfulButNoRemainingStudentsShouldReturnMethodNotAllowed() throws Exception {
        when(studentService.deleteStudent(1L)).thenReturn(true);
        when(studentService.readStudents(isNull(), isNull(), isNull())).thenReturn(new ArrayList<>());

        mockMvc.perform(delete("/students/delete/{studentId}", 1L))
                .andExpect(status().isMethodNotAllowed());
    }

}
