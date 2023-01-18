package com.khrychov.task5rest.repository;

import com.khrychov.task5rest.data.TaskData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskData, Long> {

    List<TaskData> findAllByToDoDataId(Long id);

    Optional<TaskData> findByIdAndToDoDataId(Long id, Long toDoId);

    Page<TaskData> findByPriorityAndStateAndToDoDataId(String priority, String state, Long id, Pageable pageable);
}
