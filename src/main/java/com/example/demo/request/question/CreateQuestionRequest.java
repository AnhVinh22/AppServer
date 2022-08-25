package com.example.demo.request.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {
    private String categoryId;
    private String contentQuestion;
    private String contentAnswer;
}
