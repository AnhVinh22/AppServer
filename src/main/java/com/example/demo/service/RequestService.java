package com.example.demo.service;

import com.example.demo.constant.RequestStatus;
import com.example.demo.request.Request.CreateLeaveRequest;
import com.example.demo.request.Request.CreateSoonLateRequest;
import com.example.demo.request.Request.UpdateStatusRequest;
import com.example.demo.response.ListRequestResponse;
import com.example.demo.response.RequestByUserIdResponse;
import com.example.demo.response.RequestResponse;

import java.util.List;


public interface RequestService {
    RequestResponse insertLateSoonRequest(String userId, CreateSoonLateRequest request);

    RequestResponse insertLeaveRequest(String userId, CreateLeaveRequest createLeaveRequest);

    List<RequestByUserIdResponse> findListRequestByUserId(String id);

    List<RequestByUserIdResponse> findListRequestByReceiverEmail(String id);

    ListRequestResponse approveRequest(String requestId, String token, UpdateStatusRequest status);


}
