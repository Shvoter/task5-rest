package com.khrychov.task5rest.controller;

import com.khrychov.task5rest.dto.ToDoDetailsDto;
import com.khrychov.task5rest.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @GetMapping
    public List<ToDoDetailsDto> getAllTodos() {
        return toDoService.getAll();
    }

    @GetMapping("/{id}")
    public ToDoDetailsDto getToDo(@PathVariable Long id) {
        return toDoService.getById(id);
    }
}
