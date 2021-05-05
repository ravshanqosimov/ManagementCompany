package uz.java.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.TaskDto;
import uz.java.hr.payload.TurniketDto;
import uz.java.hr.service.TurniketService;

@RestController
@RequestMapping("/api/turniket")
public class TurniketController {
    @Autowired
    TurniketService turniketService;

    @PostMapping()
    public HttpEntity<?>addTurniket(@RequestBody TurniketDto turniketDto) {
        ApiResponse apiResponse = turniketService.createTurniket(turniketDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


}
