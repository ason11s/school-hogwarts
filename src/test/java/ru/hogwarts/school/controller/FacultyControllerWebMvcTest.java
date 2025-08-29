package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "red");
        when(facultyService.create(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(
                        post("/faculty")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(faculty))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "red");
        when(facultyService.get(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void updateFacultyTest() throws Exception {
        Faculty updatedFaculty = new Faculty(1L, "Ravenclaw", "blue");
        when(facultyService.update(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(
                        put("/faculty")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(updatedFaculty))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ravenclaw"))
                .andExpect(jsonPath("$.color").value("blue"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFacultiesTest() throws Exception {
        Collection<Faculty> faculties = List.of(
                new Faculty(1L, "Gryffindor", "red"),
                new Faculty(2L, "Ravenclaw", "blue")
        );
        when(facultyService.getAll()).thenReturn(faculties);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[1].name").value("Ravenclaw"));
    }

    @Test
    void findByColorTest() throws Exception {
        Collection<Faculty> faculties = List.of(
                new Faculty(1L, "Gryffindor", "red"),
                new Faculty(2L, "Slytherin", "green")
        );
        when(facultyService.findByColor(anyString())).thenReturn(faculties.stream().filter(f -> f.getColor().equals("red")).toList());

        mockMvc.perform(get("/faculty/by-color").param("color", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[0].color").value("red"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void findByNameOrColorTest() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Gryffindor", "red")
        );

        when(facultyService.findByNameOrColor(anyString())).thenReturn(faculties);

        mockMvc.perform(get("/faculty/find").param("query", "Gryffindor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }
}
