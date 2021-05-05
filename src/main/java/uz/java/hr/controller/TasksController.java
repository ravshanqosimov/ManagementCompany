package uz.java.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.hr.entity.Task;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.TaskDto;
//import uz.java.hr.repository.TaskRepository;
import uz.java.hr.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TasksController {
    @Autowired
    TaskService taskService;


    @PostMapping
    public HttpEntity<?> addTask(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.add(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());
    }

    @PutMapping("/editTask")
    public HttpEntity<?> completeTask(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.complete(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getMessage());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getByTaskName(@PathVariable UUID id) {
        ApiResponse apiResponse = taskService.getOne(id);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }


    @GetMapping()
    public HttpEntity<?> getByTaskName() {
        return ResponseEntity.ok(taskService.getAll());
    }



}
