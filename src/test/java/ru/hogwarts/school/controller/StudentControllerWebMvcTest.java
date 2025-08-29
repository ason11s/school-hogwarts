package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void createStudentTest() throws Exception {
        Student student = new Student(1L, "Harry Potter", 12);
        when(studentService.create(any(Student.class))).thenReturn(student);
        mockMvc.perform(
                        post("/student")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(student))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void getStudentTest() throws Exception {
        Student student = new Student(1L, "Harry Potter", 12);

        when(studentService.get(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Harry Potter"));
    }

    @Test
    void updateStudentTest() throws Exception {
        Student updatedStudent = new Student(1L, "Ron Weasley", 13);
        when(studentService.update(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(
                        put("/student")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(updatedStudent))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ron Weasley"))
                .andExpect(jsonPath("$.age").value(13));
    }

    @Test
    void deleteStudentTest() throws Exception {

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }
}