package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }


    public Optional<Student> get(Long id) {
        return studentRepository.findById(id);
    }

    public Student update(Student student) {
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        return null;
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }


    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long id) {
        return studentRepository.findById(id).map(Student::getFaculty).orElse(null);
    }
}

