package org.finos.fluxnova.bpm.demo.salesreps.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.TaskService;
import org.finos.fluxnova.bpm.engine.history.HistoricVariableInstance;
import org.finos.fluxnova.bpm.engine.runtime.ProcessInstance;
import org.finos.fluxnova.bpm.engine.task.Task;

@RestController
public class ProcessController {

    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final TaskService taskService;

    public ProcessController(
            RuntimeService runtimeService,
            HistoryService historyService,
            TaskService taskService
    ) {
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.taskService = taskService;
    }

    /*
     V2
    */
    @PostMapping("/rate")
    public Map<String, Object> rate(@RequestBody Map<String, Object> body) {

        String channel = (String) body.get("channel");
        String transcript = (String) body.get("transcript");

        Map<String, Object> variables = new HashMap<>();
        variables.put("channel", channel);
        variables.put("transcript", transcript);

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                "salesRepRatingProcessV2",
                variables
        );

        HistoricVariableInstance ratingJsonVariable = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .variableName("ratingJson")
                .singleResult();

        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", instance.getProcessInstanceId());
        response.put("ratingJson", ratingJsonVariable != null ? ratingJsonVariable.getValue() : null);

        return response;
    }

    /*
     V3 start
    */
    @PostMapping("/rate/v3")
    public Map<String, Object> startV3(@RequestBody Map<String, Object> body) {

        String channel = (String) body.get("channel");
        String transcript = (String) body.get("transcript");

        Map<String, Object> variables = new HashMap<>();
        variables.put("channel", channel);
        variables.put("transcript", transcript);

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                "salesRepRatingProcessV3",
                variables
        );

        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", instance.getProcessInstanceId());
        response.put("message", "Process started. Waiting for manager review.");

        return response;
    }

    @GetMapping("/rate/v3/{processInstanceId}/tasks")
    public List<Map<String, Object>> getManagerTasks(
            @PathVariable("processInstanceId") String processInstanceId
    ) {

        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

        HistoricVariableInstance ratingJsonVariable = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("ratingJson")
                .singleResult();

        Object ratingJson = ratingJsonVariable != null ? ratingJsonVariable.getValue() : null;

        return tasks.stream().map(task -> {
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("taskId", task.getId());
            taskMap.put("taskName", task.getName());
            taskMap.put("processInstanceId", task.getProcessInstanceId());
            taskMap.put("ratingJson", ratingJson);
            return taskMap;
        }).collect(Collectors.toList());
    }

    @PostMapping("/rate/v3/tasks/{taskId}/review")
    public Map<String, Object> reviewTask(
            @PathVariable("taskId") String taskId,
            @RequestBody Map<String, Object> body
    ) {

        Boolean managerApproved = (Boolean) body.get("managerApproved");
        String managerReviewReason = (String) body.get("managerReviewReason");

        Map<String, Object> variables = new HashMap<>();
        variables.put("managerApproved", managerApproved);
        variables.put("managerReviewReason", managerReviewReason);

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        String processInstanceId = task.getProcessInstanceId();

        taskService.complete(taskId, variables);

        HistoricVariableInstance ratingJsonVariable = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("ratingJson")
                .singleResult();

        HistoricVariableInstance educationalCommentVariable = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("educationalComment")
                .singleResult();

        List<Task> remainingTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstanceId);
        response.put("ratingJson", ratingJsonVariable != null ? ratingJsonVariable.getValue() : null);
        response.put("educationalComment", educationalCommentVariable != null ? educationalCommentVariable.getValue() : null);
        response.put("remainingTasks", remainingTasks.stream().map(t -> {
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("taskId", t.getId());
            taskMap.put("taskName", t.getName());
            return taskMap;
        }).collect(Collectors.toList()));

        return response;
    }

    @PostMapping("/review/employee")
    public Map<String, Object> reviewEmployee() {

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                "salesRepPeriodicReviewProcessV1"
        );

        HistoricVariableInstance recommendationJsonVariable = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .variableName("employeeRecommendationJson")
                .singleResult();

        HistoricVariableInstance recommendationVariable = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .variableName("employeeRecommendation")
                .singleResult();

        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", instance.getProcessInstanceId());
        response.put("employeeRecommendationJson",
                recommendationJsonVariable != null ? recommendationJsonVariable.getValue() : null);
        response.put("employeeRecommendation",
                recommendationVariable != null ? recommendationVariable.getValue() : null);

        return response;
    }
}