package uz.java.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.java.hr.component.Checking;
import uz.java.hr.component.EmailSender;
import uz.java.hr.entity.*;
import uz.java.hr.entity.template.Model;
import uz.java.hr.entity.template.Worker;
import uz.java.hr.enums.TaskStatus;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.LoginDto;
import uz.java.hr.payload.RegisterDto;
import uz.java.hr.repository.RoleRepository;
//import uz.java.hr.repository.TaskRepository;
import uz.java.hr.repository.TasksRepository;
import uz.java.hr.repository.TurniketHistoryRepository;
import uz.java.hr.repository.UserRepository;
import uz.java.hr.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    Checking checking;
    @Autowired
    EmailSender emailSender;
    @Autowired
    TurniketHistoryRepository historyRepository;
    @Autowired
    TasksRepository taskRepository;

    public ApiResponse registerUser(RegisterDto registerDto) {
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) return new ApiResponse("bunday foydalanuvchi mavjud", false);

        //yagi xodim rollarini tekshirish
        Set<Integer> dtoRolesId = registerDto.getRolesId(); // yangi xodimning rollari IDisi
        for (Integer integer : dtoRolesId) {
            Optional<Role> optionalRole = roleRepository.findById(integer);
            if (!optionalRole.isPresent()) return new ApiResponse("bunday role ID mavjud emas", false);
            break;
        }

        //yangi xodim rollarini Set<Role> o`girish
        Set<Role> roles = new HashSet<>();
        for (Integer integer : dtoRolesId) {
            Optional<Role> optionalRole = roleRepository.findById(integer);
            optionalRole.ifPresent(roles::add);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            User userInTheSystem = (User) authentication.getPrincipal();
            String userPosition = userInTheSystem.getPosition();//yangi xodim qo`shmoqci bo`lgan userni pozitsiyasi
            Set<Role> userRoles = userInTheSystem.getRoles();// yangi xodim qo`shmoqci bo`lgan userni rollari

            boolean checkingRole = checking.checkingRole(userRoles, userPosition, dtoRolesId);
            if (checkingRole) {
                User user = new User();
                user.setFullName(registerDto.getFullName());
                user.setEmail(registerDto.getEmail());
                user.setPosition(registerDto.getPosition());
                user.setRoles(roles);
                Random rand = new Random();
                String password = String.format("%04d", rand.nextInt(10000));
                user.setPassword(passwordEncoder.encode(password));
                user.setEmailCode(UUID.randomUUID().toString());
                userRepository.save(user);

                boolean b1 = emailSender.mailText(user.getEmail(), user.getEmailCode(), password);
                if (b1) return new ApiResponse("yangi xodim emailiga xabar yuborildi", true);

                return new ApiResponse("xatolik yuz berdi", false);
            }
            return new ApiResponse("siz bunday role dagi userni qo`sha olmaysiz", false);
        }
        return new ApiResponse("siz user qo`sha olmaysiz", false);
    }


    public ApiResponse editPassword(LoginDto loginDto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);

        Optional<User> optionalUser = userRepository.findByEmail(loginDto.getEmail());
        if (optionalUser.isPresent() && optionalUser.get().getEmail().equals(email)) {
            User user = optionalUser.get();
            user.setPassword(loginDto.getPassword());
            userRepository.save(user);
            return new ApiResponse("parol o`zgartirildi", true);
        }
        return new ApiResponse("emailda xatolik", false);
    }

    public ApiResponse deleteUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("tizimda bunday xodim mavjud emas", false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            User userInTheSystem = (User) authentication.getPrincipal();
            User xodim = optionalUser.get();
            if (xodim.getEmail().equals(userInTheSystem.getEmail())) {
                boolean b = checking.checkingForDeleteUser(userInTheSystem.getRoles(), userInTheSystem.getPosition(), xodim.getRoles());

                if (b) {
                    userRepository.deleteById(xodim.getId());
                    return new ApiResponse("xodim o`chirildi", true);
                }
                return new ApiResponse("sizning huquqingiz to`gri kelmaydi", false);
            }
            return new ApiResponse("siz o`zingizni o`chira olmaysiz", false);
        }
        return new ApiResponse("avval tizimdan ro`yxatdan o`ting", false);
    }

    public ApiResponse getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication();
        boolean b = checking.checkingForGetUser(user.getRoles());
        if (b || user.getPosition().equalsIgnoreCase(
                "ht_manager")) {
            List<User> all = userRepository.findAll();
            //barcha xodimlar ro`yxati
            List<String> usersName = new ArrayList<>();
            for (User user1 : all) {
                usersName.add(user1.getFullName());
            }

            return new ApiResponse("xodimlar ro`yxati", true, usersName);
        }
        return new ApiResponse("siz xodimlar ro`yxatini ko`ra olmaysiz", false);
    }

    public ApiResponse getOne  (Model employeeModel) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication();
        boolean b = checking.checkingForGetUser(user.getRoles());
        if (!b) return new ApiResponse("siz bu malumotlani ololmaysiz", false);
        List<TurniketHistory> all = historyRepository.findAll();
        for (TurniketHistory turniketHistory : all) {
            Timestamp timestamp = turniketHistory.getTimestamp();

            int taqqoslash1 = timestamp.compareTo(employeeModel.getStartTime());
            int taqqoslash2 = timestamp.compareTo(employeeModel.getEndTime());
            if (taqqoslash1 >= 0 || taqqoslash2 <= 0) {
                //bizga so`rovga javob bo`ladigan xodimlarning turniktlari
                Turniket turniket = turniketHistory.getTurniket();
                User xodim = turniket.getOwner();
                Optional<Task> byTaskTakerAndTaskStatus = taskRepository.findByTaskTakerAndTaskStatus(xodim, TaskStatus.COMPLETED);

                Worker worker = new Worker();
                worker.setFullName(xodim.getFullName());
                worker.setEmail(xodim.getEmail());
                worker.setTurniketHistory(turniketHistory);
                worker.setTaskStatus(byTaskTakerAndTaskStatus.get().getTaskStatus());
                return new ApiResponse("natijalar: ", true, worker);
            }
        }
        return new ApiResponse("natija topilmadi", false);
    }
}
