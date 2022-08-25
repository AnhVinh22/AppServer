package com.example.demo.repository;

import com.example.demo.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    void deleteAllByEntityId(String entityId);
}
