package com.example.monday.excetionhandler;

public class InvalidStudentNameException extends RuntimeException {
    public InvalidStudentNameException(String s) {
        super(s);
    }
}
