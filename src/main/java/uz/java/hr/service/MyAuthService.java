package uz.java.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.java.hr.entity.Task;
import uz.java.hr.entity.User;
import uz.java.hr.enums.TaskStatus;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.LoginDto;
//import uz.java.hr.repository.TaskRepository;
import uz.java.hr.repository.TasksRepository;
import uz.java.hr.repository.UserRepository;
import uz.java.hr.security.JwtProvider;

import java.util.Optional;

@Service
public class MyAuthService implements UserDetailsService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TasksRepository  taskRepository;

    public ApiResponse login(LoginDto loginDto) {

        try {
            Authentication authenticate =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                                    loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername(), user.getRoles());
            return new ApiResponse("token", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("parol yoki login xato", false);
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("account tasdiqlandi", true);
        }
        return new ApiResponse("account allaqachon tasdiqlangan", false);
    }


    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(s);
        if (optionalUser.isPresent())
            return optionalUser.get();
        else throw new UsernameNotFoundException(s + " topilmadi");
    }


    public ApiResponse verifyTask(String takerEmail, String taskName) {
        Optional<User> optionalUser = userRepository.findByEmail(takerEmail);
        if (!optionalUser.isPresent()) return new ApiResponse("bunday email mavjud emas", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals(
                "anonymousUser")) {
            User userInTheSystem = (User) authentication.getPrincipal();
            if (userInTheSystem.getEmail().equals(takerEmail)) {
                User user = optionalUser.get();
                Optional<Task> optionalTask = taskRepository.findByTaskTakerAndName(user, taskName);
                if (optionalTask.isPresent()) {
                    Task task = optionalTask.get();
                    task.setTaskStatus(TaskStatus.PROGRESS);
                    taskRepository.save(task);
                    return new ApiResponse("vazifa tasdiqlandi", true);
                }
            }
            return new ApiResponse("emailingizni ro`yxatdan o`tkazing", false);
        }
        return new ApiResponse("vazifani qabul qilganligingizni tasdiqlash uchun avval ro`yxatdan o`tgan bo`lishingiz" +
                " kerak",
                false); }

}
