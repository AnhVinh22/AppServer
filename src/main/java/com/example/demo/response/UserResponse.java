package com.example.demo.response;

import com.example.demo.constant.AccountStatus;
import com.example.demo.constant.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String name;

    private String avatar;
    private AccountStatus accountStatus;

    public UserResponse(String id, String avatar, String name, String email, ERole role, AccountStatus accountStatus, Object o) {
        this.name = name;
        this.avatar = avatar;
        this.accountStatus = accountStatus;
    }
}
