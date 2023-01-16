package com.khrychov.task5rest.service.impl;

import com.khrychov.task5rest.data.TaskData;
import com.khrychov.task5rest.data.ToDoData;
import com.khrychov.task5rest.dto.TaskDetailsDto;
import com.khrychov.task5rest.dto.TaskSaveDto;
import com.khrychov.task5rest.dto.TaskUpdateDto;
import com.khrychov.task5rest.exception.NullEntityReferenceException;
import com.khrychov.task5rest.repository.TaskRepository;
import com.khrychov.task5rest.repository.ToDoRepository;
import com.khrychov.task5rest.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ToDoRepository toDoRepository;

    @Transactional
    @Override
    public Long create(TaskSaveDto taskSaveDto, Long toDoId) {
        ToDoData toDoData = toDoRepository.findById(toDoId).orElseThrow(
                () -> new NullEntityReferenceException("Todo with id " + toDoId + " not found"));

        TaskData taskData = new TaskData();
        taskData.setTitle(taskSaveDto.getTitle());
        taskData.setState(taskSaveDto.getState());
        taskData.setPriority(taskSaveDto.getPriority());
        taskData.setToDoData(toDoData);
        taskData.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(taskData).getId();
    }

    @Override
    public TaskDetailsDto getById(Long id) {
        TaskData taskData = taskRepository.findById(id).orElseThrow(
                () -> new NullEntityReferenceException("Task with id " + id + " not found"));
        return new TaskDetailsDto(taskData);
    }

    @Transactional
    @Override
    public void update(Long id, TaskUpdateDto taskUpdateDto) {
        if (!isFilled(taskUpdateDto)) {
            return;
        }

        TaskData taskData = taskRepository.findById(id).orElseThrow(
                () -> new NullEntityReferenceException("Task with id " + id + " not found"));

        if (taskUpdateDto.getToDoId() != null) {
            ToDoData toDoData = toDoRepository.findById(taskUpdateDto.getToDoId()).orElseThrow(
                    () -> new NullEntityReferenceException("ToDo with id " + id + " not found"));
            taskData.setToDoData(toDoData);
        }

        if (taskUpdateDto.getPriority() != null) {
            taskData.setPriority(taskUpdateDto.getPriority());
        }

        if (taskUpdateDto.getState() != null) {
            taskData.setState(taskUpdateDto.getState());
        }

        taskData.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(taskData);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDetailsDto> findAllByToDoDataId(Long id) {
        return taskRepository.findAllByToDoDataId(id).stream()
                .map(TaskDetailsDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDetailsDto> findByPriorityAndStateAndToDoDataId(String state, String priority,  Long id, int page, int size) {
        Pageable paging = PageRequest.of(page, size);

        Page<TaskData> taskDataPage = taskRepository.findByPriorityAndStateAndToDoDataId(priority, state, id, paging);
        return taskDataPage.getContent().stream()
                .map(TaskDetailsDto::new)
                .collect(Collectors.toList());
    }

    private boolean isFilled(TaskUpdateDto taskUpdateDto) {
        return taskUpdateDto.getState() != null ||
                taskUpdateDto.getPriority() != null ||
                taskUpdateDto.getToDoId() != null;
    }
}
