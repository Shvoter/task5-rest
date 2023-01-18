package com.khrychov.task5rest.controller;

import com.khrychov.task5rest.dto.*;
import com.khrychov.task5rest.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos/{todo_id}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskDetailsDto> getAllTasks(@PathVariable(name = "todo_id") Long id) {
        return taskService.findAllByToDoDataId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createTask(
            @PathVariable(name = "todo_id") Long id,
            @Valid @RequestBody TaskSaveDto dto
    ) {
        return new RestResponse(taskService.create(dto, id).toString());
    }

    @GetMapping("/{id}")
    public TaskDetailsDto getTask(@PathVariable("todo_id") Long toDoId, @PathVariable Long id) {
        return taskService.findByIdAndToDoDataId(id, toDoId);
    }

    @PutMapping("/{id}")
    public RestResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDto dto
    ) {
        taskService.update(id, dto);
        return new RestResponse("updated task with id " + id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/search")
    public List<TaskDetailsDto> getTasksByPriorityAndState(
            @PathVariable(name = "todo_id") Long id,
            @Valid @RequestBody TaskSearchDto taskSearchDto
    ) {
        return taskService.findByPriorityAndStateAndToDoDataId(
                taskSearchDto.getState(),
                taskSearchDto.getPriority(),
                id,
                taskSearchDto.getPage(),
                taskSearchDto.getSize()
        );
    }
}
