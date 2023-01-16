package com.khrychov.task5rest.repository;

import com.khrychov.task5rest.data.ToDoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ToDoRepository extends JpaRepository<ToDoData, Long> {

}
