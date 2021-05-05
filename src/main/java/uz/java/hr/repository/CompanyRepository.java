package uz.java.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.hr.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Integer>  {
}
