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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ToDoRepository toDoRepository;

    @Transactional
    @Override
    public Long create(TaskSaveDto taskSaveDto, Long toDoId) {
        ToDoData toDoData = UnboxOptional(toDoRepository.findById(toDoId), toDoId, ToDoData.class);

        TaskData taskData = new TaskData();
        taskData.setTitle(taskSaveDto.getTitle());
        taskData.setState(taskSaveDto.getState());
        taskData.setPriority(taskSaveDto.getPriority());
        taskData.setToDoData(toDoData);
        taskData.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(taskData).getId();
    }

    @Transactional
    @Override
    public void update(Long id, TaskUpdateDto taskUpdateDto) {
        if (!isFilled(taskUpdateDto)) {
            return;
        }
        TaskData taskData = UnboxOptional(taskRepository.findById(id), id, TaskData.class);
        ToDoData toDoData;

        if (taskUpdateDto.getToDoId() != null) {
            toDoData = UnboxOptional(toDoRepository.findById(taskUpdateDto.getToDoId()), id, ToDoData.class);
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

    @Override
    public TaskDetailsDto findByIdAndToDoDataId(Long id, Long toDoId) {
        TaskData taskData = UnboxOptional(taskRepository.findByIdAndToDoDataId(id, toDoId), id, TaskData.class);
        return new TaskDetailsDto(taskData);
    }

    private boolean isFilled(TaskUpdateDto taskUpdateDto) {
        return taskUpdateDto.getState() != null ||
                taskUpdateDto.getPriority() != null ||
                taskUpdateDto.getToDoId() != null;
    }

    private <T> T UnboxOptional(Optional<T> data, Long id, Class<T> aClass) {
        return data.orElseThrow(
                () -> new NullEntityReferenceException(
                        aClass.getSimpleName().replace("Data", "")+ " with id " + id + " not found"));
    }
}
