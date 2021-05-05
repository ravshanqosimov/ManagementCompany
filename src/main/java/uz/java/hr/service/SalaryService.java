package uz.java.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.java.hr.component.Checking;
import uz.java.hr.entity.SalaryTaken;
import uz.java.hr.entity.User;
import uz.java.hr.enums.MonthName;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.payload.SalaryDto;
import uz.java.hr.repository.SalaryRepository;
import uz.java.hr.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Checking checking;
    @Autowired
    SalaryRepository salaryRepository;

    public ApiResponse add(SalaryDto salaryDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = userRepository.findByEmail(salaryDto.getEmail());
        if (!optionalUser.isPresent()) return new ApiResponse("bunday email topilmadi", false);

        boolean b = checking.checkForSalary(user.getRoles(), user.getPosition());
        if (!b) return new ApiResponse("siz ushbu so`rovni amalga oshira olmaysiz", false);

        MonthName monthName = checking.checkingMonth(salaryDto.getPeriod());
        if (monthName == null) return new ApiResponse("noto`g`ri oy nomi", false);

        User worker = optionalUser.get();

        SalaryTaken salaryTaken = new SalaryTaken();
        salaryTaken.setOwner(worker);
        salaryTaken.setAmount(salaryTaken.getAmount());
        salaryTaken.setPeriod(monthName);
        salaryRepository.save(salaryTaken);
        return new ApiResponse("saqlandi", true);
    }

    public ApiResponse giveSalary(String email, String month) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) return new ApiResponse("bunday email topilmadi", false);

        boolean b = checking.checkForSalary(user.getRoles(), user.getPosition());
        if (!b) return new ApiResponse("siz ushbu so`rovni amalga oshira olmaysiz", false);
        if (checking.checkingMonth(month) == null) return new ApiResponse("noto`g`ri oy nomi", false);

        User worker = optionalUser.get();
        SalaryTaken salaryTaken = salaryRepository.findByOwner(worker).get();
        salaryTaken.setPaid(true);
        salaryRepository.save(salaryTaken);
        return new ApiResponse("success", true);
    }

    public List<SalaryTaken> getByMonths(String months) {
        MonthName monthName = checking.checkingMonth(months);
        if (monthName == null) return null;
        return salaryRepository.findAllByPeriod(monthName);
    }

    public Optional<SalaryTaken> getByOwnerId(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (!byEmail.isPresent()) return Optional.empty();
        Optional<SalaryTaken> byOwner = salaryRepository.findByOwner(byEmail.get());
        return byOwner;

    }

}
