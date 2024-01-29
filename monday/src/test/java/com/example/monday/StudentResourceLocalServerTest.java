package com.example.monday;

import com.example.monday.excetionhandler.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.*;

//definiujemy kontekst springa wraz z tymczasowym serwerem in memory o losowym porcie
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentResourceLocalServerTest {

    @LocalServerPort //używamy adnotację, żeby zapisać port serwera
    private int port;

    @Autowired //wstrzykujemy tego beana abu móc wykonać zapytanie restowe do naszego serwera
    private TestRestTemplate restTemplate;

    @Test
    void givenNoStudent_whenGetById_ThenReturnNotFound() {
        //definiujemy zapytanie, metoda wywołana powinna odpowiadać metodzoe restowej
        //adres url jest pełny, zewnętrzny - nie tylko część aplikacyjna
        //podajemy także obiekt, na który można przemapować odpowiedź w formie json, takk aby odczytać dane
        var responseEntity = restTemplate.getForEntity("http://localhost:"+ port+"/students/0b0dba77-e141-4dae-960d-344eabcbb858", ErrorResponse.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(404));
        var errorResponse = responseEntity.getBody();
        assertTrue(errorResponse.message().contains("not found"));
        assertNotNull(errorResponse.id());
        assertNotNull(errorResponse.timestamp());
    }
}
