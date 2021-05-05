package uz.java.hr.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class RegisterDto {
    @Size(min = 3, max = 50)
    private String fullName;

    @Email
    @NotNull
    private String email;

    private String position;

    @NotNull
    private Set <Integer>  rolesId;


}
