package com.khrychov.task5rest.service;

import com.khrychov.task5rest.dto.TaskDetailsDto;
import com.khrychov.task5rest.dto.TaskSaveDto;
import com.khrychov.task5rest.dto.TaskUpdateDto;

import java.util.List;

public interface TaskService {

    Long create(TaskSaveDto taskSaveDto, Long toDoId);

    void update(Long id, TaskUpdateDto taskUpdateDto);

    void delete(Long id);

    TaskDetailsDto findByIdAndToDoDataId(Long id, Long toDoId);

    List<TaskDetailsDto> findAllByToDoDataId(Long id);

    List<TaskDetailsDto> findByPriorityAndStateAndToDoDataId(String state, String priority,  Long id, int page, int size);
}
