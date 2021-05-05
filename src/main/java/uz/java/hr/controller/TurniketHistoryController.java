package uz.java.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.TurniketHistoryDto;
import uz.java.hr.service.TurniketHistoryService;

@RestController
@RequestMapping("/api/turniketHistory")
public class TurniketHistoryController {
    @Autowired
    TurniketHistoryService historyService;

    @PostMapping
    public HttpEntity<?> add(@RequestBody TurniketHistoryDto historyDto) {
        ApiResponse apiResponse = historyService.add(historyDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());


    }


}
