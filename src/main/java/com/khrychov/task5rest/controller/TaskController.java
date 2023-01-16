package com.khrychov.task5rest.controller;

import com.khrychov.task5rest.dto.*;
import com.khrychov.task5rest.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            @Valid @RequestBody TaskSaveDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().toString());
        }
        return new RestResponse(taskService.create(dto, id).toString());
    }

    @GetMapping("/{id}")
    public TaskDetailsDto getTask(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    public RestResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().toString());
        }
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
            @Valid @RequestBody TaskSearchDto taskSearchDto,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().toString());
        }
        return taskService.findByPriorityAndStateAndToDoDataId(
                taskSearchDto.getState(),
                taskSearchDto.getPriority(),
                id,
                taskSearchDto.getPage(),
                taskSearchDto.getSize()
        );
    }
}
