package com.example.demo.service;

import com.example.demo.request.question.CreateQuestionRequest;
import com.example.demo.request.question.UpdateQuestionRequest;
import com.example.demo.response.QuestionResponse;

import java.util.List;

public interface QuestionService {
    QuestionResponse insert(String token, CreateQuestionRequest createQuestionRequest);

    List<QuestionResponse> getListMyQuestion(String token);

    QuestionResponse getQuestionById(String questionId);

    QuestionResponse repQuestionOfUser(String token, UpdateQuestionRequest updateQuestionRequest, String questionId);

}
