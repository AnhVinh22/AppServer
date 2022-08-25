package com.example.demo.controller;

import com.example.demo.constant.AccountStatus;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.login.LoginRequest;
import com.example.demo.response.LoginResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController()
@RequestMapping("demo/v1/tokens/")
@RequiredArgsConstructor
public class TokenController {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponseObject(HttpStatus.NOT_ACCEPTABLE.value(),
                            "wrong email", null));
        }

        User user = userOpt.get();

        if (!user.getPassword().equalsIgnoreCase(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).
                    body(new ResponseObject(HttpStatus.NOT_ACCEPTABLE.value(),
                            "wrong password format", null));
        }

        if (user.getAccountStatus().toString().equals(AccountStatus.INACTIVE.toString())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).
                    body(new ResponseObject(HttpStatus.NOT_ACCEPTABLE.value(),
                            "User inactive", null));
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());
        LoginResponse loginResponse = new LoginResponse(user.getId(),
                user.getName(), user.getEmail(), user.getRole(), token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Login success", loginResponse));
    }
}
