package uz.java.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.java.hr.entity.Turniket;
import uz.java.hr.entity.TurniketHistory;
import uz.java.hr.entity.User;
import uz.java.hr.enums.TurniketType;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.TurniketHistoryDto;
import uz.java.hr.repository.TurniketHistoryRepository;
import uz.java.hr.repository.TurniketRepository;

import java.util.Optional;

@Service
public class TurniketHistoryService {
    @Autowired
    TurniketHistoryRepository historyRepository;
    @Autowired
    TurniketRepository turniketRepository;

    public ApiResponse add(TurniketHistoryDto historyDto) {
        Optional<Turniket> optionalTurniket = turniketRepository.findByTurniketCode(historyDto.getTurniketCode());
        if (!optionalTurniket.isPresent())
            return new ApiResponse("turniket topilmadi", false);
        Turniket turniket = optionalTurniket.get();
        User userInTheSys = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!turniket.getOwner().getEmail().equals(userInTheSys.getEmail())) return new ApiResponse("ushbu turniket " +
                "sizga tegishli emas", false);
        TurniketHistory turniketHistory = new TurniketHistory();
        turniketHistory.setTurniket(turniket);
//agar xodim avval korxonaga kirgan bo`lsa chiqdi degan statusni beradi yoki aksincha
        Optional<TurniketHistory> optionalTurniketHistory = historyRepository.findByTurniket(turniket);
        if (optionalTurniketHistory.isPresent()) {
            if (optionalTurniketHistory.get().getTurniketType().equals(TurniketType.STATUS_IN))
                turniketHistory.setTurniketType(TurniketType.STATUS_OUT);
            else turniketHistory.setTurniketType(TurniketType.STATUS_IN);
        } else turniketHistory.setTurniketType(TurniketType.STATUS_IN);
        historyRepository.save(turniketHistory);
        return new ApiResponse("success", true);

    }




}
