package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    public void setup() {
        facultyRepository.deleteAll();
    }

    @Test
    void createFacultyTest(){
        Faculty faculty = new Faculty(null, "Gryffindor", "red");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }
    @Test
    void getFacultyTest() {
        Faculty faculty = new Faculty(null, "Hufflepuff", "yellow");
        ResponseEntity<Faculty> createdResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        assertNotNull(createdResponse.getBody());
        Long facultyId = createdResponse.getBody().getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + facultyId, Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(facultyId, response.getBody().getId());
        assertEquals("Hufflepuff", response.getBody().getName());
    }
    @Test
    void updateFacultyTest() {
        Faculty faculty = new Faculty(null, "Ravenclaw", "blue");
        ResponseEntity<Faculty> createdResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        assertNotNull(createdResponse.getBody());
        Faculty createdFaculty = createdResponse.getBody();

        createdFaculty.setName("Ravenclaw House");
        createdFaculty.setColor("navy");

        restTemplate.put("/faculty", createdFaculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + createdFaculty.getId(), Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ravenclaw House", response.getBody().getName());
        assertEquals("navy", response.getBody().getColor());
    }
    @Test
    void deleteFacultyTest() {
        Faculty faculty = new Faculty(null, "Slytherin", "green");
        ResponseEntity<Faculty> createdResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        assertNotNull(createdResponse.getBody());
        Long facultyId = createdResponse.getBody().getId();

        restTemplate.delete("/faculty/" + facultyId);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void findByColorTest() {

        restTemplate.postForEntity("/faculty", new Faculty(null, "Gryffindor", "red"), Faculty.class);
        restTemplate.postForEntity("/faculty", new Faculty(null, "Ravenclaw", "blue"), Faculty.class);
        restTemplate.postForEntity("/faculty", new Faculty(null, "Slytherin", "red"), Faculty.class);


        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("/faculty/by-color?color=red", Faculty[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }
}