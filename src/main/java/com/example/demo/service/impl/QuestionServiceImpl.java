package com.example.demo.service.impl;

import com.example.demo.constant.ERole;
import com.example.demo.converter.QuestionConverter;
import com.example.demo.domain.Category;
import com.example.demo.domain.Question;
import com.example.demo.exception.NewsNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.request.question.CreateQuestionRequest;
import com.example.demo.request.question.UpdateQuestionRequest;
import com.example.demo.response.QuestionResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.QuestionService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final JwtService jwtService;

    private final QuestionRepository questionRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public QuestionResponse insert(String token, CreateQuestionRequest createQuestionRequest) {
        JwtData data = jwtService.parseToken(token);
        ERole role = data.getRole();
        String userId = data.getUserId();
        if (role.equals(ERole.ADMIN)) {
            Question question = new Question();
            question.setUserId(userId);
            question.setCategoryId(createQuestionRequest.getCategoryId());
            question.setContentQuestion(createQuestionRequest.getContentQuestion());
            question.setContentAnswer(createQuestionRequest.getContentAnswer());
            question.setCreateTime(System.currentTimeMillis());
            Category category = categoryRepository.findById(createQuestionRequest.getCategoryId()).get();
            category.getQuestionsList().add(question);
            categoryRepository.save(category);
            questionRepository.save(question);
            return QuestionConverter.ConvertQuestionToQuestionReponse(question);
        }

        if (role.equals(ERole.USER)) {
            Question question = new Question();
            question.setUserId(userId);
            question.setContentQuestion(createQuestionRequest.getContentQuestion());
            question.setCreateTime(System.currentTimeMillis());
            questionRepository.save(question);
            return QuestionConverter.ConvertQuestionToQuestionReponse(question);
        }
        return null;
    }

    @Override
    public List<QuestionResponse> getListMyQuestion(String token) {
        JwtData data = jwtService.parseToken(token);
        String userId = data.getUserId();
        List<QuestionResponse> collect = questionRepository.findAllByUserIdOrderByCreateTimeDesc(userId)
                .stream()
                .map(question -> {
                    return QuestionConverter.ConvertQuestionToQuestionReponse(question);
                })
                .collect(Collectors.toList());
        return null;
    }

    @Override
    public QuestionResponse getQuestionById(String questionId) {
        Question question = questionRepository.findById(questionId).get();
        if (question == null) {
            throw new NewsNotFoundException("QUESTION ID DOES NOT EXIST");
        }
        QuestionResponse questionResponse = QuestionResponse.builder()
                .contentQuestion(question.getContentQuestion())
                .contentAnswer(question.getContentAnswer())
                .build();
        return questionResponse;
    }

    @Override
    public QuestionResponse repQuestionOfUser(String token, UpdateQuestionRequest updateQuestionRequest, String questionId) {
        JwtData data = jwtService.parseToken(token);
        String userId = data.getUserId();
        Optional<Question> question = questionRepository.findById(questionId);
        Question question1 = question.get();
        question1.setUserId(userId);
        question1.setContentAnswer(updateQuestionRequest.getContentAnswer());
        questionRepository.save(question1);
        return QuestionConverter.ConvertQuestionToQuestionReponse(question1);
    }
}