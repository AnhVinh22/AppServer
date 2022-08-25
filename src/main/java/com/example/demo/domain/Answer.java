package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Answer {
    @Id
    private String id;
    private String questionId;
    private String answerUserId;
    private String content;
    private Long createTime;
    private Long lastUpdateTime;
}
