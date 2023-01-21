package com.khrychov.task5rest.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TaskUpdateDto {

    @Size(max = 40, message = "task state is too long")
    private String state;

    @Size(max = 40, message = "task priority is too long")
    private String priority;

    private Long toDoId;
}
