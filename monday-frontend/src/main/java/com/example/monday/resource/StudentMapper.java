package com.example.monday.resource;

import com.example.monday.data.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getName(),
                student.getUnit(),
                student.getIndex()
        );
    }

    public Student toEntity(CreateStudent createStudent) {
        return new Student(createStudent.getName(), createStudent.getUnit());
    }
}
