package uz.java.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.hr.entity.Turniket;
import uz.java.hr.entity.TurniketHistory;

import java.sql.Timestamp;
import java.util.Optional;

public interface TurniketHistoryRepository extends JpaRepository<TurniketHistory, Integer> {

    Optional<TurniketHistory> findByTurniket(Turniket turniket);



}
