package uz.java.hr.payload;

import lombok.Data;
import uz.java.hr.entity.User;
import uz.java.hr.enums.MonthName;

@Data
public class SalaryDto {
    private String email;
    private double amount;
    private String period;


}
