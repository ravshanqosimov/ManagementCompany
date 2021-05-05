package uz.java.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sun.plugin.util.UIUtil;
import uz.java.hr.component.Checking;
import uz.java.hr.component.EmailSender;
import uz.java.hr.entity.Task;
import uz.java.hr.entity.User;
import uz.java.hr.enums.TaskStatus;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.TaskDto;
//import uz.java.hr.repository.TaskRepository;
import uz.java.hr.repository.TasksRepository;
import uz.java.hr.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TasksRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Checking checking;
    @Autowired
    EmailSender emailSender;

    public ApiResponse add(TaskDto taskDto) {
        Optional<User> optionalUser = userRepository.findByEmail(taskDto.getTaskTakerEmail());
        if (!optionalUser.isPresent()) return new ApiResponse("vazifaini qabul qiluvchi email topilmadi ", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals(
                "anonymousUser")) {
            User user = (User) authentication.getPrincipal();
            User takerUser = optionalUser.get();
            boolean b = checking.checkingTask(user.getRoles(), takerUser.getRoles());
            if (b) {
                Task task = new Task();
                task.setName(taskDto.getTaskName());
                task.setDescription(taskDto.getDescription());
                task.setDeadline(taskDto.getDeadline());
                task.setTaskTaker(takerUser);
                task.setTaskGiver(user);
                task.setTaskStatus(TaskStatus.NEW);
                taskRepository.save(task);
                boolean send = emailSender.givingTaskForEmail(takerUser.getEmail(), task.getName(), user.getEmail(),
                        task.getDescription(), task.getDeadline());
                if (send) return new ApiResponse("yangi xodim emailiga xabar yuborildi", true);
                return new ApiResponse("xatolik yuz berdi", false);
            }
            return new ApiResponse("siz bu huquqga ega emassiz", false);
        }
        return new ApiResponse("siz ro`yxatdan o`tmagansiz", false);
    }


    public ApiResponse complete(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals(
                "anonymousUser")) {
            User userInTheSys = (User) authentication.getPrincipal();
            Optional<Task> optionalTask = taskRepository.findByTaskTakerAndName(userInTheSys, taskDto.getTaskName());
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                task.setTaskStatus(TaskStatus.COMPLETED);
                task.setAnswer(taskDto.getAnswer());
                taskRepository.save(task);
                boolean b = emailSender.mailCompleteTask(task.getTaskGiver().getEmail(), task.getName(), task.getAnswer(),
                        task.getTaskTaker().getEmail());
                if (b) {
                    return new ApiResponse("xabat yuborildi", true);
                }
                return new ApiResponse("xatolik yuz berdi", false);


            }
            return new ApiResponse("task topilmadi", false);


        }
        return new ApiResponse("siz ro`yxatdan o`tmagansiz", false);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public ApiResponse getOne(UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(task -> new ApiResponse("natija: ", true, task)).orElseGet(() -> new ApiResponse("task topilmadi", false));
    }
}
