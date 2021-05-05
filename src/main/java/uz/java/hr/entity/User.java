package uz.java.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.java.hr.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User extends AbsEntity implements UserDetails {


    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role > roles;

    @Email
    @Column(unique = true, nullable = false)
    private String email;
    private String emailCode;
    private String position;


    private boolean accountNonExpired = true;//userning amal qilish muddati o`tmagan?
    private boolean accountNonLocked = true; //bu user qulflanmagan?
    private boolean credentialsNonExpired = true; //userning ishonchliligi muddati o`tgan o`tmaganligi?
    private boolean enabled;//userning aktiv yo aktiv emasligi

    @Override
    //userning huquqlari
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public User(String fullName, String password, Set<Role> roles, @Email String email, String position, boolean enabled) {
        this.fullName = fullName;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.position = position;
        this.enabled = enabled;
    }
}
