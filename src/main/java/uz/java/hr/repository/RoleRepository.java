package uz.java.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.hr.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

//boolean existsById(Integer id);
}
