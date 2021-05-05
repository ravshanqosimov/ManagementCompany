package uz.java.hr.entity.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.java.hr.entity.TurniketHistory;
import uz.java.hr.enums.TaskStatus;

@Data
public class Worker {
    private String fullName;
    private String email;
    private TurniketHistory turniketHistory;
    private TaskStatus taskStatus;


}
