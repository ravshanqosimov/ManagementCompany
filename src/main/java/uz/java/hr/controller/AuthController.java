package uz.java.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.LoginDto;
import uz.java.hr.service.MyAuthService;
import uz.java.hr.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    MyAuthService myAuthService;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = myAuthService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        ApiResponse apiResponse = myAuthService.verifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getMessage());

    }

    @GetMapping("/verifyTask")
    public HttpEntity<?> verifyTask(@RequestParam String takerEmail, @RequestParam String taskName) {
        ApiResponse apiResponse = myAuthService.verifyTask(takerEmail, taskName);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getMessage());
    }


}
