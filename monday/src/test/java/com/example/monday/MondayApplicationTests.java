package com.example.monday;

import com.example.monday.data.Student;
import com.example.monday.data.StudentRepository;
import com.example.monday.data.StudentUnit;
import com.example.monday.excetionhandler.RecordNotFoundException;
import com.example.monday.resource.StudentDto;
import com.example.monday.resource.StudentResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //Uruchamiamy testowy kontekst springa, w ramach którego możemy wykonywać testy
@AutoConfigureMockMvc //Definiujemy, że chemy skorzystać ze skonfigurowanego obiektu mockMvc.
class MondayApplicationTests {

    @Autowired//wstrzykujemy bean - jeśli istnieje kontekst spring ai bean jest skonfigurowany poprawnie powinna się tu znaleźć instancja klasy
    private StudentResource studentResource;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //czyszczenie danych wprowadzonych w ramach testów, pozwala nam na zachowanie ich niezależności
    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThrows(RecordNotFoundException.class,
                () -> studentResource.getStudentById(UUID.randomUUID()));
    }

    @Test
    void givenNoStudents_whenGetById_thenRespondWithNotFound() throws Exception {
        var response = mockMvc.perform(get("/students/0b0dba77-e141-4dae-960d-344eabcbb858"))
                .andDo(print())
                .andReturn().getResponse();


        assertEquals(response.getStatus(), 404);
        assertTrue(response.getContentAsString().contains("not found"));
    }

    @SneakyThrows
    @Test
    void givenStudentsWithDifferentName_whenGetByName_ThenReturnStudentWithGivenName(){
        var student1 = new Student("Karola", StudentUnit.GDANSK, 11L);
        var student2 = new Student("Aga", StudentUnit.GDANSK, 12L);
        studentRepository.saveAll(List.of(student1, student2));

        var response = mockMvc.perform(get("/students?name=Karola"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var studentList = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<StudentDto>>() {});
        assertEquals(1, studentList.size());
        var returnedStudent = studentList.get(0);
        assertEquals(student1.getId(), returnedStudent.id());
    }

    @SneakyThrows
    @Test
    void givenStudentsWithDifferentName_whenGetByName_ThenReturnStudentWithGivenNameFromGDANSK(){
        var student1 = new Student("Karola", StudentUnit.GDANSK, 11L);
        var student2 = new Student("Karola", StudentUnit.WARSZAWA, 13L);
        var student3 = new Student("Aga", StudentUnit.GDANSK, 12L);
        studentRepository.saveAll(List.of(student1, student2, student3));

        mockMvc.perform(get("/students?name=Karola"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"Karola\"")))
                .andExpect(content().string(not(containsString("\"name\":\"Aga\""))));
    }

    @SneakyThrows
    @Test
    void givenStudentsWithDifferentName_whenDeleteByName_ThenDeleteStudentWithGivenName(){
        var student1 = new Student("Karola", StudentUnit.GDANSK, 11L);
        var student2 = new Student("Karola", StudentUnit.WARSZAWA, 13L);
        var student3 = new Student("Aga", StudentUnit.GDANSK, 12L);
        studentRepository.saveAll(List.of(student1, student2, student3));

        mockMvc.perform(delete("/students?name=Karola"))
                .andExpect(status().isNoContent());

        assertTrue(studentRepository.getAllByName("Karola").isEmpty());
    }

    @SneakyThrows
    @Test
    void givenStudentsWithDifferentName_whenDeleteByNotExistingName_ThenRespondWithBadRequest(){
        mockMvc.perform(delete("/students?name=Patryk"))
                .andExpect(status().isBadRequest());
    }

}
