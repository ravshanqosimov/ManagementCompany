package uz.java.hr.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    private String email;
    @NotNull
    private String password;
}
