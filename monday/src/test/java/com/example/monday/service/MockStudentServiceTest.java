package com.example.monday.service;

import com.example.monday.data.Student;
import com.example.monday.data.StudentDataComponent;
import com.example.monday.data.StudentRepository;
import com.example.monday.data.StudentUnit;
import com.example.monday.resource.CreateStudent;
import com.example.monday.resource.StudentMapper;
import com.example.monday.resource.StudentResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//Rozszerzamy junit rozszerzeniem z mockito, aby móc w junitowych testach korzystać z funkcji biblioteki mockito
@ExtendWith(MockitoExtension.class)
class MockStudentServiceTest {

    //Mock tworzy nam proxy naszej klasy - to sprawia, że wywołania tej klasy nie wykonają rzeczywistej metody
    //i każdorazowe jej wywołanie musimy skonfigurować, możemy też tak jak w przypadku Spy śledzić jej wywołania

    private StudentRepository studentRepository = mock(StudentRepository.class);

    private StudentDataComponent studentDataComponent = new StudentDataComponent();

    private StudentMapper studentMapper = new StudentMapper();

    //InjectMocks pozwala nam stworzyć klasę testowaną z wykorzystaniem obiektów, które zdefiniowaliśmy
    //jako elementy mockito używając adnotacji Mock/Spy
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, studentMapper);
    }


    @Test
    void givenGdanskUnitWhenSaveStudentThenGetValidIndex() {
        //given
        var student = new CreateStudent("Karola", StudentUnit.GDANSK);
        /**poniżej przykład konfigurowania zachowania mocka przy wywołaniu konkrentej metody
         */
        when(studentRepository.getMaxIndex()).thenReturn(Optional.of(5L));

        //when
        var savedStudent = studentService.saveStudent(student);

        //then
        assertEquals(student.getName(), savedStudent.getName());
        assertEquals(student.getUnit(), savedStudent.getUnit());
        assertEquals(10, savedStudent.getIndex());
        verify(studentRepository, times(1)).save(any());
    }


    @Test
    void givenWarszawaUnitWhenSaveStudentThenGetValidIndex() {
        //given
        var student = new CreateStudent("Karola", StudentUnit.WARSZAWA);
        /** poniżej przykład konfigurowania zachowania mocka przy wywołaniu konkrentej metody
        zachowanie analogiczne jak powyżej, inny zapis
         */
        doReturn(Optional.of(7L)).when(studentRepository).getMaxIndex();

        //when
        studentService.saveStudent(student);

        //then
        /** ArgumentCaptor pozwala nam odczytać wartość parametru przekazanego do metody wywołanej w ramach mocka
         */
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository, times(1)).save(argumentCaptor.capture());
        var savedStudent = argumentCaptor.getValue();
        assertEquals(student.getUnit(), savedStudent.getName());
        assertEquals(student.getUnit(), savedStudent.getUnit());
        assertEquals(70, savedStudent.getIndex());
    }

    //test sprawdzajacy czy metoda getStudentByEmail zwraca poprawnego studenta
    @Test
    void givenValidEmailWhenGetStudentByEmailThenReturnStudent() {

        String email = "karola@example.com";
        Student student = new Student("Karola", StudentUnit.GDANSK, 1L, email, "123456789");
        studentDataComponent.saveStudent(student);

        Student result = studentDataComponent.getStudentByEmail(email);

        assertEquals(student.getEmail(), result.getEmail());
    }

    //Sprawdza czy update poprawnie aktualizuje studenta
    @Test
    void givenValidIdWhenUpdateStudentThenStudentIsUpdated() {
        UUID id = UUID.randomUUID();
        Student student = new Student("Karola", StudentUnit.GDANSK, 1L, "karola@example.com", "123456789");
        studentDataComponent.saveStudent(student);
        Student updatedStudent = new Student("Karola", StudentUnit.WARSZAWA, 2L, "karola2@example.com", "987654321");

        studentDataComponent.updateStudent(id, updatedStudent);
        Student result = studentDataComponent.getStudentById(id);

        assertEquals(updatedStudent.getName(), result.getName());
        assertEquals(updatedStudent.getUnit(), result.getUnit());
        assertEquals(updatedStudent.getIndex(), result.getIndex());
        assertEquals(updatedStudent.getEmail(), result.getEmail());
        assertEquals(updatedStudent.getPhoneNumber(), result.getPhoneNumber());
    }

    //Sprawdza czy metoda getStudentByPhoneNumber zwraca poprawnego studenta

    @Test
    void givenValidPhoneNumberWhenGetStudentByPhoneNumberThenReturnStudent() {

        String phoneNumber = "123456789";
        Student student = new Student("Karola", StudentUnit.GDANSK, 1L, "karola@example.com", phoneNumber);
        studentDataComponent.saveStudent(student);

        Student result = studentDataComponent.getStudentByPhoneNumber(phoneNumber);

        assertEquals(student.getPhoneNumber(), result.getPhoneNumber());
    }
}
