package idespring.lab1.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.Data;

//@AllArgsConstructor
//@NoArgsConstructor
//@Data
public class Student {
    @NotNull
    @NotEmpty
    private String name;

    public Student(String name, int age, long studentId, List<Integer> marks) {
        this.name = name;
        this.age = age;
        this.studentId = studentId;
        this.marks = marks;
    }

    public Student() {}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 12, message = "age must be at least equals 12")
    @Max(value = 100, message = "Age must be less than or equal to 100")
    private int age;

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @PositiveOrZero
    private long studentId;

    public long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    @NotEmpty(message = "Marks cannot be empty")
    @Size(min = 1, message = "There should be at least one mark")
    @Valid
    private List<@Min(value = 0, message = "Invalid mark, must be >= 0")
        @Max(value = 10, message = "Invalid mark, must be <= 10") Integer>
            marks = new ArrayList<>();

    public List<Integer> getMarks() {
        return this.marks;
    }

    public void setMarks(List<Integer> marks) {
        this.marks = marks;
    }
}
