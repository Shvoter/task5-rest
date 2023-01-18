package com.khrychov.task5rest;

import com.khrychov.task5rest.dto.ToDoDetailsDto;
import com.khrychov.task5rest.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ToDoControllerTests {

    @Autowired
    private  MockMvc mvc;

    @Autowired
    private  ToDoRepository toDoRepository;

    @Test
    public void getAllToDosGetMappingTest() throws Exception {
        List<ToDoDetailsDto> toDoDetailsDtos = toDoRepository.findAll().stream()
                .map(ToDoDetailsDto::new)
                .toList();

        mvc.perform(MockMvcRequestBuilders.get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(toDoData("$[0]", toDoDetailsDtos.get(0)))
                .andExpect(toDoData("$[1]", toDoDetailsDtos.get(1)))
                .andExpect(toDoData("$[2]", toDoDetailsDtos.get(2)));
    }

    @Test
    public void getToDoByExistIdGetMappingTest() throws Exception {
        ToDoDetailsDto toDoDetailsDto = new ToDoDetailsDto(toDoRepository.findById(1L).get());

        mvc.perform(MockMvcRequestBuilders.get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(toDoData("$", toDoDetailsDto));
    }

    @Test
    public void getToDoByNotExistIdGetMappingTest() throws Exception {
        long notExistId = 6L;

        mvc.perform(MockMvcRequestBuilders.get("/api/todos/" + notExistId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("Todo with id " + notExistId + " not found"));
    }

    public static ResultMatcher toDoData(String prefix, ToDoDetailsDto toDoDetailsDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(toDoDetailsDto.getId()),
                jsonPath(prefix + ".title").value(toDoDetailsDto.getTitle()));
    }
}
