package uz.java.hr.payload;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
public class TaskDto {

    @NotNull
    private String taskName;
    @NotNull
    private String description;
    @NotNull
    private Timestamp deadline;
    private String answer;
    private String taskTakerEmail;
}
