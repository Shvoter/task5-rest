package com.khrychov.task5rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TaskSearchDto {

    @NotNull(message = "task state should not be null")
    @Size(max = 40, message = "task title is too long")
    private String state;

    @NotNull(message = "task priority should not be null")
    @Size(max = 40, message = "task priority is too long")
    private String priority;

    @NotNull(message = "page should not be null")
    private int page;

    @NotNull(message = "size should not be null")
    private int size;
}
