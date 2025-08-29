package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setup() {
        studentRepository.deleteAll();
    }


    @Test
    void createStudentTest(){
        Student student = new Student(null, "Harry Potter", 12);
        ResponseEntity<Student> response = restTemplate.postForEntity("/student", student, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }
    @Test
    void getStudentTest(){
        Student student = new Student(null, "Harry Potter", 12);
        ResponseEntity<Student> createdResponse = restTemplate.postForEntity("/student", student, Student.class);
        assertNotNull(createdResponse.getBody());
        Long studentId = createdResponse.getBody().getId();

        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + studentId, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
    }
    @Test
    void updateStudentTest(){
        Student student = new Student(null, "Harry Potter", 12);
        ResponseEntity<Student> createdResponse = restTemplate.postForEntity("/student", student, Student.class);
        assertNotNull(createdResponse.getBody());
        Student createdStudent = createdResponse.getBody();

        createdStudent.setName("Ron Weasley");
        createdStudent.setAge(13);
        restTemplate.put("/student", createdStudent);
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + createdStudent.getId(), Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ron Weasley", response.getBody().getName());
        assertEquals(13, response.getBody().getAge());
    }
    @Test
    void deleteStudentTest(){
        Student student = new Student(null, "Harry Potter", 12);
        ResponseEntity<Student> createdResponse = restTemplate.postForEntity("/student", student, Student.class);
        assertNotNull(createdResponse.getBody());
        Long studentId = createdResponse.getBody().getId();
        restTemplate.delete("/student/" + studentId);
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + studentId, Student.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getStudentsByAgeTest() {
        restTemplate.postForEntity("/student", new Student(null, "Student1", 12), Student.class);
        restTemplate.postForEntity("/student", new Student(null, "Student2", 13), Student.class);
        restTemplate.postForEntity("/student", new Student(null, "Student3", 12), Student.class);


        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student/age/12", Student[].class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length); // Ожидаем 2 студентов
    }
}