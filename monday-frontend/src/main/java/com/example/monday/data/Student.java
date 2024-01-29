package com.example.monday.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    public Student(String name, StudentUnit unit) {
        this.name = name;
        this.unit = unit;

    }
    public Student(String name, StudentUnit unit, Long index) {
        this.name = name;
        this.unit = unit;
        this.index = index;
    }
    public Student(UUID id, String name, StudentUnit unit, long index) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.index = index;
    }

    @Id
    @GeneratedValue
    private UUID id;
    @Setter
    private String name;
    @Enumerated(EnumType.STRING)
    @Setter
    private StudentUnit unit;
    @Setter
    private Long index;
    @Setter
    private String email;
    @Setter
    private String phoneNumber;


}
