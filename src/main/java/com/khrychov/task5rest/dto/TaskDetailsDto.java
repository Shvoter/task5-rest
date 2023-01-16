package com.khrychov.task5rest.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.khrychov.task5rest.data.TaskData;
import lombok.*;

import java.time.LocalDateTime;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class TaskDetailsDto {

    Long id;

    String title;

    String state;

    String priority;

    LocalDateTime updatedAt;

    Long todoId;

    public TaskDetailsDto(TaskData taskData) {
        this.id = taskData.getId();
        this.title = taskData.getTitle();
        this.state = taskData.getState();
        this.priority = taskData.getPriority();
        this.updatedAt = taskData.getUpdatedAt();
        this.todoId = taskData.getToDoData().getId();
    }
}
