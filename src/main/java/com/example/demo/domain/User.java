package com.example.demo.domain;

import com.example.demo.constant.AccountStatus;
import com.example.demo.constant.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
    @Id
    private String id;
    private String avatar;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private ERole role;
    private AccountStatus accountStatus;
}
