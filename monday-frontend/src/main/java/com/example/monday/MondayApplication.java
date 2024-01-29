package com.example.monday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Adnotacja mówiąca o tym, że tu znajduje się główna klasa naszej aplikacji. To tu jest ona uruchamiana
// i od tego miejsca w szerz oraz w głąb skanowane są pakiety. Skanowanie nigdy nie jest wykonywane w górę.
@SpringBootApplication
public class MondayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MondayApplication.class, args);
    }

}
