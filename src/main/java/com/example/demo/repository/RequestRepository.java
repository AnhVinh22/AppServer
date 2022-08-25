package com.example.demo.repository;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import com.example.demo.domain.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {

    Request findFirstByUserIdOrderByIdDesc(String userId);

    List<Request> findRequestsByYearRequestAndRequestTypeAndUserId(int year, RequestType type, String userId);

    List<Request> findRequestsByUserIdOrderByCreateTimeAsc(String userId);

    List<Request> findRequestsByReceiverEmailAndRequestStatus(String email, RequestStatus status);

    Optional<Request> findRequestsByIdAndReceiverEmail(String requestId, String email);

}
