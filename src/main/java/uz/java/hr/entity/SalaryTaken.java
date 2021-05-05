package uz.java.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.java.hr.entity.template.AbsEntity;
import uz.java.hr.enums.MonthName;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SalaryTaken extends AbsEntity {

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    private MonthName period;

    private boolean paid = false; //oy uchun to'langanlik holati

}
