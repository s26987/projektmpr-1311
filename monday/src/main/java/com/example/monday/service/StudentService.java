package com.example.monday.service;

import com.example.monday.data.Student;
import com.example.monday.data.StudentRepository;
import com.example.monday.data.StudentUnit;
import com.example.monday.excetionhandler.RecordNotFoundException;
import com.example.monday.resource.CreateStudent;
import com.example.monday.resource.StudentDto;
import com.example.monday.resource.StudentMapper;
import com.example.monday.excetionhandler.InvalidStudentNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// Tu korzystamy już z Dependency Injection realizowanego przez konstruktor
// Pozwalamy, aby to Spring utworzył dla nas i wstrzykął StudentRepository do tej klasy
// podając to jako parametr konstruktora. Dopuszcza się też wstrzyknięcie Beana przez setter z adnotacją @Autowired
// Zamiast konstruktora używamy adnotacji z pakietu Lombok - dzięki temu jeśli dodamy tu inny Bean nie musimy już nic robić - konstruktor zaktualizuje nam lombok.
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;


    public Student saveStudent(CreateStudent createStudent) {
        var toSave = studentMapper.toEntity(createStudent);
        var index = createIndex(createStudent.getUnit());
        toSave.setIndex(index);
        studentRepository.save(toSave);
        return toSave;
    }

    public StudentDto getStudentById(UUID id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new RecordNotFoundException("Student with id " + id + " not found"));
    }

    public void deleteByName(String name){
        var studentsByName = studentRepository.getAllByName(name);
        if(studentsByName.isEmpty()) {
            throw new InvalidStudentNameException("Student with name=" + name + " not exists.");
        }
        studentRepository.deleteAll(studentsByName);
    }

    private Long createIndex(StudentUnit unit) {
        long maxIndex = studentRepository.getMaxIndex().orElse(0L);
        if(StudentUnit.GDANSK.equals(unit)) {
            return 5 + maxIndex;
        } else {
            return 10 * maxIndex;
        }
    }

    public List<StudentDto> getStudentsByName(String name) {
        return studentRepository.getFromGdanskByName(name)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }
}
