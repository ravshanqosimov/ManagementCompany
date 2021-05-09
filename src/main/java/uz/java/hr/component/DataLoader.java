package uz.java.hr.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.java.hr.entity.Company;
import uz.java.hr.entity.Role;
import uz.java.hr.entity.User;
import uz.java.hr.enums.RoleName;
import uz.java.hr.repository.CompanyRepository;
import uz.java.hr.repository.RoleRepository;
import uz.java.hr.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRepository companyRepository;

        @Value("${spring.datasource.initialization-mode}")
        private String initialMode;


    @Override
    public void run(String... args) throws Exception {


        if (initialMode.equals("always")) {

            Role directorRole = new Role();
            Role manager = new Role();
            Role staff = new Role();
            manager.setRoleName(RoleName.ROLE_MANAGER);
            directorRole.setRoleName(RoleName.ROLE_DIRECTOR);
            staff.setRoleName(RoleName.ROLE_STAFF);


            roleRepository.save(directorRole);
            roleRepository.save(manager);
            roleRepository.save(staff);

            User user = new User("Odilbek Mirzayev",
                    passwordEncoder.encode("pdp123"),
                    Collections.singleton(roleRepository.findById(1).get()),
                    "birnima@gmail.com",
                    "kompaniya direktori",
                    true);
            userRepository.save(user);


            Company company = new Company(user, "ECMA");
            companyRepository.save(company);

        }
    }
}
