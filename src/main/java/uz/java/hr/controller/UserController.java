package uz.java.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.hr.entity.template.Model;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.LoginDto;
import uz.java.hr.payload.RegisterDto;
import uz.java.hr.service.MyAuthService;
import uz.java.hr.service.UserService;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    MyAuthService myAuthService;

    @PostMapping("/register")
    public HttpEntity<?> addUser(@RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = userService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }

    //xodimlar o`z parolini o`zgartirishi mumkin
    @PutMapping("/editPassword")
    public HttpEntity<?> editPassword(@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = userService.editPassword(loginDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getMessage());
    }

    //direktor hamda HR_manager uchun
    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = userService.getAll();
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }

//    xodimning belgilangan oraliq vaqt bo’yicha ishga kelib-ketishi
    @PostMapping("/getOne")
    public HttpEntity<?> getOneUser(@RequestBody Model model) {
        ApiResponse apiResponse = userService.getOne(model);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }


    //xodimlarni o`chirish
    //ammo xodim o`zini o`zi o`chira olmaydi
    @DeleteMapping("/delete/{email}")
    public HttpEntity<?> deleteUser(@PathVariable String email) {
        ApiResponse apiResponse = userService.deleteUser(email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse.getMessage());
    }


}
