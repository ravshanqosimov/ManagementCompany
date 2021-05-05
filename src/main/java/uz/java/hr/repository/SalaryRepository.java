package uz.java.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.hr.entity.SalaryTaken;
import uz.java.hr.entity.User;
import uz.java.hr.enums.MonthName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<SalaryTaken, UUID> {
    Optional<SalaryTaken> findByOwner(User owner);

    List<SalaryTaken> findAllByPeriod(MonthName period);

}
