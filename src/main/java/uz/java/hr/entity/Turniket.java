package uz.java.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.java.hr.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Turniket extends AbsEntity {
    @OneToOne
    private User owner;

    private String turniketCode = UUID.randomUUID().toString();


}
