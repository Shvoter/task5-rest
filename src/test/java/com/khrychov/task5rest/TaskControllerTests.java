package com.khrychov.task5rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khrychov.task5rest.data.TaskData;
import com.khrychov.task5rest.dto.TaskDetailsDto;
import com.khrychov.task5rest.dto.TaskSaveDto;
import com.khrychov.task5rest.dto.TaskSearchDto;
import com.khrychov.task5rest.dto.TaskUpdateDto;
import com.khrychov.task5rest.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.function.Function;



@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void getAllTasksGetMappingTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";
        List<TaskDetailsDto> taskDetailsDtos = taskRepository.findAllByToDoDataId(toDoId).stream()
                .map(TaskDetailsDto::new)
                .toList();

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(taskData("$[0]", taskDetailsDtos.get(0)))
                .andExpect(taskData("$[1]", taskDetailsDtos.get(1)));
    }

    @Test
    @Transactional
    public void createTaskPostMappingTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";
        TaskSaveDto taskSaveDto = new TaskSaveDto("testTitle", "testState", "testPriority");

        getHttpRequest(HttpMethod.POST, url, taskSaveDto)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists());

        List<TaskData> allTasks = taskRepository.findAll();
        TaskData taskData = allTasks.get(allTasks.size() - 1);
        TaskSaveDto savedDto = new TaskSaveDto(taskData.getTitle(), taskData.getState(), taskData.getPriority());

        Assertions.assertEquals(taskSaveDto, savedDto);
    }

    @Test
    public void createTaskWithBlankTitleTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";

        TaskSaveDto taskSaveDtoWithBlankTitle = new TaskSaveDto();
        taskSaveDtoWithBlankTitle.setState("testState");
        taskSaveDtoWithBlankTitle.setPriority("testPriority");

        String ErrorMessage = "task title is required";

        getHttpRequest(HttpMethod.POST, url, taskSaveDtoWithBlankTitle)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(ErrorMessage));
    }

    @Test
    public void createTaskWithToLongTitleTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";
        TaskSaveDto taskWithToLongTitle = new TaskSaveDto("testLongTitle testLongTitle testLongTitle",
                "testState", "testPriority");

        String ErrorMessage = "task title is too long";

        getHttpRequest(HttpMethod.POST, url, taskWithToLongTitle)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(ErrorMessage));
    }

    @Test
    public void createTaskWithNullStateTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";

        TaskSaveDto taskSaveDtoWithNullState= new TaskSaveDto();
        taskSaveDtoWithNullState.setTitle("testTitle");
        taskSaveDtoWithNullState.setPriority("testPriority");

        String ErrorMessage = "task state should not be null";

        getHttpRequest(HttpMethod.POST, url, taskSaveDtoWithNullState)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(ErrorMessage));
    }

    @Test
    public void createTaskWithToLongStateTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";
        TaskSaveDto taskWithToLongState = new TaskSaveDto("testTitle",
                "testLongState testLongState testLongState testLongState", "testPriority");

        String ErrorMessage = "task state is too long";

        getHttpRequest(HttpMethod.POST, url, taskWithToLongState)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(ErrorMessage));
    }

    @Test
    public void createTaskWithNullPriorityTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";

        TaskSaveDto taskSaveDtoWithNullPriority= new TaskSaveDto();
        taskSaveDtoWithNullPriority.setTitle("testTitle");
        taskSaveDtoWithNullPriority.setState("testState");

        String ErrorMessage = "task priority should not be null";

        getHttpRequest(HttpMethod.POST, url, taskSaveDtoWithNullPriority)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(ErrorMessage));
    }

    @Test
    public void createTaskWithToLongPriorityTest() throws Exception {
        long toDoId = 3;
        String url = "/api/todos/" + toDoId + "/tasks";
        TaskSaveDto taskWithToLongState = new TaskSaveDto("testTitle",
                "testState", "testLongPriority testLongPriority testLongPriority");

        String ErrorMessage = "task priority is too long";

        getHttpRequest(HttpMethod.POST, url, taskWithToLongState)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(ErrorMessage));
    }

    @Test
    public void getTaskByExistIdGetMappingTest() throws Exception {
        long toDoId = 3;
        long id = 26;
        String url = "/api/todos/" + toDoId + "/tasks/" + id;
        TaskDetailsDto taskDetailsDto = new TaskDetailsDto(taskRepository.findByIdAndToDoDataId(id, toDoId).get());

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(taskData("$", taskDetailsDto));
    }

    @Test
    public void getTaskByNotExistIdGetMappingTest() throws Exception {
        long toDoId = 3;
        long notExistId = 50;
        String url = "/api/todos/" + toDoId + "/tasks/" + notExistId;

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.result").value("Task with id " + notExistId + " not found"));
    }

    @Test
    @Transactional
    public void deleteTaskByIdDeleteMappingTest() throws Exception {
        long toDoId = 3;
        long id = 26;
        String url = "/api/todos/" + toDoId + "/tasks/" + id;

        mvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.result").value("deleted task with id " + id));
    }

    @Test
    public void deleteTaskByNotExistIdDeleteMappingTest() throws Exception {
        long toDoId = 3;
        long notExistId = 50;
        String url = "/api/todos/" + toDoId + "/tasks/" + notExistId;

        mvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.result").value("Task with id " + notExistId + " not found"));
    }

    @Test
    @Transactional
    public void updateTaskPutMappingTest() throws Exception {
        long toDoId = 3;
        long id = 26;
        String url = "/api/todos/" + toDoId + "/tasks/" + id;

        TaskUpdateDto taskUpdateDto = new TaskUpdateDto("test state", "test priority", 2L);

        getHttpRequest(HttpMethod.PUT, url, taskUpdateDto)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("updated task with id " + id));

        TaskDetailsDto taskDetailsDto = new TaskDetailsDto(taskRepository.findByIdAndToDoDataId(id, 2L).get());
        Assertions.assertEquals(taskUpdateDto.getState(), taskDetailsDto.getState());
        Assertions.assertEquals(taskUpdateDto.getPriority(), taskDetailsDto.getPriority());
        Assertions.assertEquals(taskUpdateDto.getToDoId(), taskDetailsDto.getToDoId());
    }

    @Test
    public void updateTaskWithNotExistId() throws  Exception {
        long toDoId = 3;
        long id = 50;
        String url = "/api/todos/" + toDoId + "/tasks/" + id;

        TaskUpdateDto taskUpdateDto = new TaskUpdateDto("test state", "test priority", 2L);

        getHttpRequest(HttpMethod.PUT, url, taskUpdateDto)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Task with id " + id + " not found"));
    }

    @Test
    public void updateTaskWithEmptyBodyRequest() throws Exception {
        long toDoId = 3;
        long id = 26;
        String url = "/api/todos/" + toDoId + "/tasks/" + id;

        TaskUpdateDto taskUpdateDto = new TaskUpdateDto();

        getHttpRequest(HttpMethod.PUT, url, taskUpdateDto)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("you did not change any data"));
    }

    @Test
    public void searchTasksByStateAndPriority() throws Exception {
        long toDoId = 2;
        String url = "/api/todos/" + toDoId + "/tasks/search";
        String state = "started";
        String priority = "medium";
        int page = 0;
        int size = 2;

        TaskSearchDto taskSearchDto = new TaskSearchDto(state, priority, page, size);
        Pageable paging = PageRequest.of(page, size);

        List<TaskDetailsDto> taskDetailsDtos = taskRepository
                .findByPriorityAndStateAndToDoDataId(priority, state, toDoId, paging).stream()
                .map(TaskDetailsDto::new)
                .toList();

        getHttpRequest(HttpMethod.POST, url, taskSearchDto)
                .andExpect(status().isOk())
                .andExpect(taskData("$[0]", taskDetailsDtos.get(0)))
                .andExpect(taskData("$[1]", taskDetailsDtos.get(1)));
    }

    private <T> ResultActions getHttpRequest(
            Function<String, MockHttpServletRequestBuilder> httpMethod, String url, T body) throws Exception {
        String json = new ObjectMapper().writeValueAsString(body);

        return mvc.perform(httpMethod.apply(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json));
    }

    static class HttpMethod {

        public static final Function<String, MockHttpServletRequestBuilder> POST =
                MockMvcRequestBuilders::post;

        public static final Function<String, MockHttpServletRequestBuilder> PUT =
                MockMvcRequestBuilders::put;
    }

    public static ResultMatcher taskData(String prefix, TaskDetailsDto taskDetailsDto) {
        return ResultMatcher.matchAll(
                MockMvcResultMatchers.jsonPath(prefix + ".id").value(taskDetailsDto.getId()),
                MockMvcResultMatchers.jsonPath(prefix + ".title").value(taskDetailsDto.getTitle()),
                MockMvcResultMatchers.jsonPath(prefix + ".state").value(taskDetailsDto.getState()),
                MockMvcResultMatchers.jsonPath(prefix + ".priority").value(taskDetailsDto.getPriority()),
                MockMvcResultMatchers.jsonPath(prefix + ".updatedAt").value(taskDetailsDto.getUpdatedAt().toString()),
                MockMvcResultMatchers.jsonPath(prefix + ".toDoId").value(taskDetailsDto.getToDoId()));
    }
}
