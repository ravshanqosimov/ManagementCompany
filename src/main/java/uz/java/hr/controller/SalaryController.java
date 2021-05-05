package uz.java.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.hr.entity.SalaryTaken;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.SalaryDto;
import uz.java.hr.service.SalaryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/salary" )

public class SalaryController {

@Autowired
SalaryService salaryService;

    @PostMapping
    public HttpEntity<?> add(@RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.add(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }
    @GetMapping("/giveSalary")
    public ResponseEntity<?> giveSalary(@RequestParam String email, @RequestParam String months)  {
        ApiResponse apiResponse = salaryService.giveSalary(email, months);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getMessage());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }

    @GetMapping("/getSalariesByMonths")
    public ResponseEntity<?> getbyMonths(@RequestParam String months) {
        List<SalaryTaken> byMonths = salaryService.getByMonths(months);
        if (byMonths.isEmpty()) return ResponseEntity.status(409).body(byMonths);
        return ResponseEntity.ok(byMonths);
    }

    @GetMapping("/getByOwnerEmail")
    public ResponseEntity<?> getOwnerByEmail(@RequestParam String email) {
        Optional<SalaryTaken> optional = salaryService.getByOwnerId(email);
        if (!optional.isPresent()) return ResponseEntity.status(409).body(optional);
        return ResponseEntity.ok(optional.get());
    }
}
