package com.example.demo.response;


import com.example.demo.constant.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @Id
    private String id;
    private String name;
    private String email;
    private ERole eRole;
    private String token;

}
