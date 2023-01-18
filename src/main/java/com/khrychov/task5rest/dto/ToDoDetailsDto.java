package com.khrychov.task5rest.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.khrychov.task5rest.data.ToDoData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;



@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@Getter
public class ToDoDetailsDto {

    Long id;

    String title;

    public ToDoDetailsDto(ToDoData toDoData) {
        this.id = toDoData.getId();
        this.title = toDoData.getTitle();
    }
}
