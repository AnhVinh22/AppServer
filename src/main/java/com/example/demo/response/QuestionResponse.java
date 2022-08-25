package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class QuestionResponse {
    private String id;
    private String createUserId;
    private String contentQuestion;
    private String contentAnswer;
    private String creatTime;
    private String categoryId;
}
