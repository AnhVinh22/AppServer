package com.example.demo.controller;

import com.example.demo.request.question.CreateQuestionRequest;
import com.example.demo.request.question.UpdateQuestionRequest;
import com.example.demo.response.QuestionResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("demo/v1/question/")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> creatQuestion(@Valid @RequestHeader("Authorization") String token,
                                                        @RequestBody CreateQuestionRequest createQuestionRequest) {
        QuestionResponse questionResponse = questionService.insert(token, createQuestionRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "CREATE QUESTION SUCCESS", questionResponse));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getListMyQuestion(@RequestHeader("Authorization") String token) {
        List<QuestionResponse> questionResponses = questionService.getListMyQuestion(token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "GET LIST MY QUESTIONS SUCCESS", questionResponses));
    }

    @GetMapping("{questionId}")
    public ResponseEntity<ResponseObject> getQuestionById(@PathVariable(name = "questionId") String questionId) {
        QuestionResponse questionResponse = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "GET QUESTION BY ID", questionResponse));
    }

    @PutMapping("{questionId}")
    public ResponseEntity<ResponseObject> repQuestionOfUser(@RequestHeader("Authorization") String token,
                                                            @RequestBody UpdateQuestionRequest updateQuestionRequest,
                                                            @PathVariable(name = "questionId") String questionId) {
        QuestionResponse questionResponse = questionService.repQuestionOfUser(token, updateQuestionRequest, questionId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "REP QUESTION OF USER SUCCESS", questionResponse));
    }
}
