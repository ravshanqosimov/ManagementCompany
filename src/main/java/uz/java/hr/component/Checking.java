package uz.java.hr.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.java.hr.entity.Role;
import uz.java.hr.enums.MonthName;
import uz.java.hr.enums.RoleName;
import uz.java.hr.payload.ApiResponse;
import uz.java.hr.repository.RoleRepository;

import java.util.Optional;
import java.util.Set;

@Component
public class Checking {
    @Autowired
    RoleRepository roleRepository;

    public boolean checkingRole(Set<Role> userRole, String userPosition, Set<Integer> newRole) {
        for (Role role : userRole) {
            String name = role.getRoleName().name();

            if (name.equals(RoleName.ROLE_DIRECTOR.name())) {
                for (Integer integer : newRole) {
                    Optional<Role> optionalRole = roleRepository.findById(integer);
                    String newName = optionalRole.get().getRoleName().name();
                    if (!newName.equals(RoleName.ROLE_DIRECTOR.name()) && !newName.equals(RoleName.ROLE_STAFF.name()))
                        return true;
                }
            }
            if (userPosition.equalsIgnoreCase("hr_manager")) {
                for (Integer integer : newRole) {
                    Optional<Role> optionalRole = roleRepository.findById(integer);
                    String newName = optionalRole.get().getRoleName().name();
                    if (!newName.equals(RoleName.ROLE_MANAGER.name()) && !newName.equals(RoleName.ROLE_DIRECTOR.name()))
                        return true;
                }
            }
        }
        return false;
    }

    public boolean checkingTask(Set<Role> userRole, Set<Role> taskTaker) {
        for (Role role : userRole) {
            String name = role.getRoleName().name();
            if (name.equals(RoleName.ROLE_DIRECTOR.name())) {
                for (Role role1 : taskTaker) {
                    String roleName = role1.getRoleName().name();
                    if (roleName.equals(RoleName.ROLE_MANAGER.name()) || roleName.equals(RoleName.ROLE_STAFF.name()))
                        return true;
                }
            }
            if (name.equals(RoleName.ROLE_MANAGER.name())) {
                for (Role role2 : taskTaker) {
                    String roleName = role2.getRoleName().name();
                    if (!roleName.equals(RoleName.ROLE_DIRECTOR.name()) && !roleName.equals(RoleName.ROLE_MANAGER.name()) && roleName.equals(RoleName.ROLE_STAFF.name()))
                        return true;
                }
            }
        }

        return false;
    }

    public boolean checkingForDeleteUser(Set<Role> userRole, String userPosition, Set<Role> newRole) {
        for (Role role : userRole) {
            String name = role.getRoleName().name();

            if (name.equals(RoleName.ROLE_DIRECTOR.name())) {
                return true;
            }
            if (userPosition.equalsIgnoreCase("hr_manager")) {
                for (Role xodimRole : newRole) {
                    String xodim = xodimRole.getRoleName().name();

                    if (!xodim.equals(RoleName.ROLE_MANAGER.name()) && !xodim.equals(RoleName.ROLE_DIRECTOR.name()))
                        return true;
                }
            }
        }
        return false;
    }

    public boolean checkingForGetUser(Set<Role> roles) {
        for (Role role : roles) {
            if (role.getRoleName().name().equals(RoleName.ROLE_DIRECTOR.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkForSalary(Set<Role> roles, String position) {
        for (Role role : roles) {
            if (role.getRoleName().name().equals(RoleName.ROLE_DIRECTOR.name()) || position.equalsIgnoreCase(
                    "hr_manager")) {
                return true;
            }
        }
        return false;
    }

    public MonthName checkingMonth(String month) {
        MonthName[] values = MonthName.values();
        for (MonthName monthName : values) {
            if (monthName.name().equalsIgnoreCase(month))
                return monthName;
        }
        return null;
    }

}
