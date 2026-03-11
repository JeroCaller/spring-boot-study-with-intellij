package com.jerocaller.AuthTokenSecurity.data.entity;

import com.jerocaller.AuthTokenSecurity.common.UserRoles;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.exception.custom.PasswordNotMatchException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 200)
    private String password;

    @Column
    private LocalDateTime lastLoginAt;

    @Column
    private int age;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(UserRoles.USER.getRoleName()));
    }

    public static User toEntity(UserRequest userRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
            .username(userRequest.getUsername())
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .age(userRequest.getAge())
            .build();
    }

    public void update(UserInfoPatchRequest patchRequest, PasswordEncoder passwordEncoder) {
        if (patchRequest.getBeforePassword() != null && patchRequest.getNextPassword() != null) {
            if (!passwordEncoder.matches(
                patchRequest.getBeforePassword(),
                this.getPassword())
            ) {
                throw new PasswordNotMatchException();
            }

            this.setPassword(passwordEncoder.encode(patchRequest.getNextPassword()));
        }

        Optional.ofNullable(patchRequest.getUsername()).ifPresent(this::setUsername);
        Optional.ofNullable(patchRequest.getAge()).ifPresent(this::setAge);
    }
}
