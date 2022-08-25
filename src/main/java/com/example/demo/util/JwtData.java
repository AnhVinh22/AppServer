package com.example.demo.util;


import com.example.demo.constant.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtData {
    private String email;
    private String userId;
    private ERole role;
}
