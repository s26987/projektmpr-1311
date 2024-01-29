package com.example.monday.data;


import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

//Adnotacja Service mówi nam, że klasa ta jest definicją springowego Beana
// i jej instancje są zarządzane przez kontext Springowy
//Domyślnie jej scope to Singleton - powstanie tylko jedna taka dla całej aplikacji.
public class StudentDataComponent {

    @Setter
    private List<Student> students = new ArrayList<>();

    public void saveStudent(Student student) {
        students.add(student);
    }

    public Student getStudentById(UUID id) {
        return students.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Long getMaxIndex() {
        return students.stream()
                .map(Student::getIndex)
                .max(Comparator.naturalOrder())
                .orElse(0L);
    }

    public void updateStudent(UUID id, Student updatedStudent) {
        Student student = getStudentById(id);
        if (student != null) {
            student.setName(updatedStudent.getName());
            student.setUnit(updatedStudent.getUnit());
            student.setIndex(updatedStudent.getIndex());
            student.setEmail(updatedStudent.getEmail());
            student.setPhoneNumber(updatedStudent.getPhoneNumber());
        }
    }

    // Wyszukuje po emailu
    public Student getStudentByEmail(String email) {
        return students.stream()
                .filter(it -> it.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Wyszukuje po numerze telefonu
    public Student getStudentByPhoneNumber(String phoneNumber) {
        return students.stream()
                .filter(it -> it.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(null);
    }
}
