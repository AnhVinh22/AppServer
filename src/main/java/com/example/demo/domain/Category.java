package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private String id;
    private String banner;
    private String title;
    private List<Question> questionsList = new ArrayList<>();
    private String createUserId;
    private String updateUserId;
    private Long createTime;
    private Long lastUpdateTime;
}
