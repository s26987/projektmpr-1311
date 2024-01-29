package com.example.monday.data;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface StudentRepository extends CrudRepository<Student, UUID> {

    @Modifying
    @Transactional
    void deleteByName(String name);

    @Query("select max(s.index) from Student s")
    Optional<Long> getMaxIndex();

    List<Student> getByNameAndUnit(String name, StudentUnit unit);

    List<Student> getAllByName(String name);

    default List<Student> getFromGdanskByName(String name) {
        return getByNameAndUnit(name, StudentUnit.GDANSK);
    }
}
