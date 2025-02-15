package idespring.lab1.controller;

import idespring.lab1.model.Student;
import idespring.lab1.service.StudentServInterface;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentServInterface studentService;

    @Autowired
    public StudentController(StudentServInterface studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/addStud")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(student));
    }

    @GetMapping("/get/{studentId}")
    public ResponseEntity<List<Student>>
        getStudentById(@Positive @NotNull @PathVariable Long studentId) {
        return !(studentService.readStudents(null, null, studentId).isEmpty())
                ? new ResponseEntity<>(studentService
                .readStudents(null, null, studentId), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Student>> getStudents(
            /*@Min(12) @Max(100)*/ @RequestParam(required = false) Integer age,
            /*@NotEmpty*/ @RequestParam(required = false) String sort) {
        return !(studentService.readStudents(age, sort, null).isEmpty())
                ? new ResponseEntity<>(studentService
                .readStudents(age, sort, null), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{studentId}")
    public ResponseEntity<List<Student>> updateStudent(@Positive @NotNull @PathVariable
                                                           Long studentId,
                                                       @Valid @RequestBody Student student) {
        return studentService.updateStudent(student, studentId)
                ? new ResponseEntity<>(studentService
                .readStudents(null, null, studentId), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<List<Student>> deleteStudent(@Positive @NotNull @PathVariable
                                                           Long studentId) {
        return studentService.deleteStudent(studentId)
                && !(studentService.readStudents(null, null, null).isEmpty())
                ? new ResponseEntity<>(studentService
                .readStudents(null, null, null), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

}
