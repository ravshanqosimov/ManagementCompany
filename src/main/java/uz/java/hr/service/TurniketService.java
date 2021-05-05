package uz.java.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.java.hr.entity.Turniket;
import uz.java.hr.entity.User;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.TurniketDto;
import uz.java.hr.repository.TurniketRepository;
import uz.java.hr.repository.UserRepository;

import java.util.Optional;

@Service
public class TurniketService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TurniketRepository turniketRepository;

    public ApiResponse createTurniket(TurniketDto turniketDto) {
        Optional<User> optionalUser = userRepository.findByEmail(turniketDto.getOwnerEmail());
        if (!optionalUser.isPresent()) return new ApiResponse("bunday email tizimda mavjud emas", false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals(
                "anonymousUser")) {
            User user = (User) authentication.getPrincipal();
            if (user.getEmail().equals(optionalUser.get().getEmail())) {
                Turniket turniket = new Turniket();
                turniket.setOwner(user);
                turniketRepository.save(turniket);
                return new ApiResponse("Turniket qo`shildi", true, turniket.getTurniketCode());
            }
            return new ApiResponse("kiritgan emailingiz tizimdagi emailingizga mos emas! ", false);
        }
        return new ApiResponse("sizga turniket berish mumkin emas, emailingizni tasdiqlamagan bo`lishingiz mumkin",
                false);
    }













}
