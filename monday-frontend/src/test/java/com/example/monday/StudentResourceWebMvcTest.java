package com.example.monday;

import com.example.monday.excetionhandler.RecordNotFoundException;
import com.example.monday.resource.StudentResource;
import com.example.monday.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentResource.class) //stawiamy tylko webowy kontekst springa, tylko z beanem klasy StudentResource
class StudentResourceWebMvcTest {

    @MockBean //mockujemy serwis, żeby móc stworzyć beana StudentResource, który go używa
    //oraz żeby móc definiować jego zachowanie
    private StudentService studentService;

    @Autowired // dociągamy automatycznie skonfigurowanego beana klasy Mock bean
    private MockMvc mockMvc;

    @Test
    void givenNoStudents_whenGetById_thenRespondWithNotFound() throws Exception {
        when(studentService.getStudentById(any())).thenThrow(new RecordNotFoundException("not found"));

        //definiujemy zapytanie restowe:
        //get - metoda Http
        //jej parametr "/students/..." - wewnętrzna ścieżka do wywołania resta
        var response = mockMvc.perform(get("/students/0b0dba77-e141-4dae-960d-344eabcbb858"))
                //wyświetlamy zapytania
                .andDo(print())
                //sprawdzamy status
                .andExpect(status().isNotFound())
                //sprawdzamy fragment body odpowiedzi
                .andExpect(content().string(containsString("not found")))
                //lub zwracamy odpowiedź i weryfikujemy jak poniżej za pomocą metod assert
                .andReturn().getResponse();


        assertEquals(response.getStatus(), 404);
        assertTrue(response.getContentAsString().contains("not found"));
    }
}
