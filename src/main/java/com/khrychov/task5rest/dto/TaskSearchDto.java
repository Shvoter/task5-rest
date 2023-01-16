package com.khrychov.task5rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSearchDto {

    @NotNull
    @Size(max = 40, message = "task title is too long")
    private String state;

    @NotNull
    @Size(max = 40, message = "task title is too long")
    private String priority;

    @NotNull
    private int page;

    @NotNull
    private int size;
}
