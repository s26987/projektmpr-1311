package com.example.monday.data;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** simple junit tests without mockito
 */
@Deprecated
class StudentRepositoryTest {

    @Test
    void shouldReturnMaxIndexWhenStudentListIsNotEmpty() {
        var studentRepository = new StudentDataComponent();
        var student = new Student(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"), "Karola", StudentUnit.GDANSK, 3L);
        studentRepository.setStudents(List.of(student));

        var maxIndex = studentRepository.getMaxIndex();

        assertEquals(student.getIndex(), maxIndex);
    }

    @Test
    void shouldReturnZeroWhenStudentListNotEmpty() {
        var studentRepository = new StudentDataComponent();

        var maxIndex = studentRepository.getMaxIndex();

        assertEquals(0L, maxIndex);
    }
}
