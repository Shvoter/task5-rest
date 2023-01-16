package com.khrychov.task5rest.service.impl;

import com.khrychov.task5rest.data.ToDoData;
import com.khrychov.task5rest.dto.ToDoDetailsDto;
import com.khrychov.task5rest.exception.NullEntityReferenceException;
import com.khrychov.task5rest.repository.ToDoRepository;
import com.khrychov.task5rest.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    @Override
    public ToDoDetailsDto getById(Long id) {
        ToDoData toDoData = toDoRepository.findById(id).orElseThrow(
                () -> new NullEntityReferenceException("Todo with id " + id + " not found"));
        return new ToDoDetailsDto(toDoData);
    }

    @Override
    public List<ToDoDetailsDto> getAll() {
        return toDoRepository.findAll().stream()
                .map(ToDoDetailsDto::new)
                .collect(Collectors.toList());
    }
}
