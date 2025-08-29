package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Optional<Student> studentOptional = studentService.get(id);
        if (studentOptional.isPresent()) {
            return ResponseEntity.ok(studentOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.update(student);
        if (updatedStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping("/age/{age}")
    public List<Student> getByAge(@PathVariable int age) {
        return studentService.findByAge(age);
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/age/between")
    public List<Student> findByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudentId(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(faculty);
    }
}
