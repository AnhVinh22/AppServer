package com.example.demo.converter;

import com.example.demo.domain.Question;
import com.example.demo.response.QuestionResponse;

public class QuestionConverter {

    public static QuestionResponse ConvertQuestionToQuestionReponse(Question question) {
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setId(question.getId());
        questionResponse.setCreateUserId(question.getId());
        questionResponse.setCreatTime(DateConvert.convertLongToDate(question.getCreateTime()));
        questionResponse.setContentQuestion(question.getContentQuestion());
        questionResponse.setContentAnswer(question.getContentAnswer());
        questionResponse.setCategoryId(question.getCategoryId());
        return questionResponse;
    }
}
