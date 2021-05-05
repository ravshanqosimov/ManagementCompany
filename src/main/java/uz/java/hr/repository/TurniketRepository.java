package uz.java.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.hr.entity.Turniket;

import java.util.Optional;
import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Turniket, UUID> {
    Optional<Turniket> findByTurniketCode(String turniketCode);

}
