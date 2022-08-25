package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    private String id;
    private String userId;
    private String categoryId;
    private String contentQuestion;
    private String contentAnswer;
    private Long createTime;
    private Long lastUpdateTime;
}
