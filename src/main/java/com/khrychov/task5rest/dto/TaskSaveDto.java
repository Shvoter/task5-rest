package com.khrychov.task5rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TaskSaveDto {

    @NotBlank(message = "task title is required")
    @Size(max = 40, message = "task title is too long")
    private String title;

    @NotNull
    @Size(max = 40, message = "task title is too long")
    private String state;

    @NotNull
    @Size(max = 40, message = "task title is too long")
    private String priority;

}
