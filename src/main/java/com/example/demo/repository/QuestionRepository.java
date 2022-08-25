package com.example.demo.repository;

import com.example.demo.domain.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

    List<Question> findAllByUserIdOrderByCreateTimeDesc(String userId);

    int countAllByCategoryId(String categoryId);

    List<Question> findAllByCategoryIdOrderByCreateTimeDesc(String categoryId);

    void deleteAllByCategoryId(String categoryId);
}
