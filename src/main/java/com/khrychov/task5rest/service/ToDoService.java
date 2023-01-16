package com.khrychov.task5rest.service;

import com.khrychov.task5rest.dto.ToDoDetailsDto;

import java.util.List;

public interface ToDoService {

    ToDoDetailsDto getById(Long id);

    List<ToDoDetailsDto> getAll();
}
