package com.example.AuthenticationService.service;

import com.example.AuthenticationService.authConfig.JwtService;
import com.example.AuthenticationService.entity.RoleEntity;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.exception.InvalidCredentialsException;
import com.example.AuthenticationService.exception.UserEmailExistsException;
import com.example.AuthenticationService.models.AuthenticationResponse;
import com.example.AuthenticationService.models.LoginRequest;
import com.example.AuthenticationService.models.RegisterRequest;
import com.example.AuthenticationService.repository.RoleRepository;
import com.example.AuthenticationService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserEmailExistsException("Email already exists");
        }

        List<String> rolesTypeOfString = registerRequest.getRoles();
        List<RoleEntity> roleEntityTypeList = new ArrayList<>();

        for(String roleName: rolesTypeOfString) {
            RoleEntity roleEn = roleRepository.findByRoleName(roleName).orElseThrow();
            roleEntityTypeList.add(roleEn);
        }

        UserEntity userEntity = UserEntity.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(roleEntityTypeList)
                .build();
        userRepository.save(userEntity);

        return AuthenticationResponse
                .builder()
                .token("Successfully Registered")
                .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail());

        if (userEntity == null) {
            throw new InvalidCredentialsException("Invalid email");
        }

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid password");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        var jwtToken = jwtService.generateToken(userEntity);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}