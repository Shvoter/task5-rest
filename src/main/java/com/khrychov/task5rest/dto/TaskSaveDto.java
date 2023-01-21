package com.khrychov.task5rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TaskSaveDto {

    @NotBlank(message = "task title is required")
    @Size(max = 40, message = "task title is too long")
    private String title;

    @NotNull(message = "task state should not be null")
    @Size(max = 40, message = "task state is too long")
    private String state;

    @NotNull(message = "task priority should not be null")
    @Size(max = 40, message = "task priority is too long")
    private String priority;

}
