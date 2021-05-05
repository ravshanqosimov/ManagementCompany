package uz.java.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.java.hr.entity.template.AbsEntity;
import uz.java.hr.enums.TaskStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Task extends AbsEntity {

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String description;

    private Timestamp deadline;

    @ManyToOne(optional = false)
    private User taskTaker;//task qabul qiluvchi

    private String answer;// xodimning bajargan vazifasi

    @ManyToOne(optional = false)
    private User taskGiver;//task beruvchi

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;


}
