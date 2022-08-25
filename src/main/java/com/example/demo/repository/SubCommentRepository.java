package com.example.demo.repository;

import com.example.demo.domain.SubComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCommentRepository extends MongoRepository<SubComment, String> {
    void deleteAllByEntityId(String entityId);
}
