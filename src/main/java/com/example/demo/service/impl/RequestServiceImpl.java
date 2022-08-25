package com.example.demo.service.impl;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import com.example.demo.converter.DateConvert;
import com.example.demo.converter.RequestChecker;
import com.example.demo.converter.RequestConverter;
import com.example.demo.domain.Request;
import com.example.demo.domain.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.Request.CreateLeaveRequest;
import com.example.demo.request.Request.CreateSoonLateRequest;
import com.example.demo.request.Request.UpdateStatusRequest;
import com.example.demo.response.ListRequestResponse;
import com.example.demo.response.RequestByUserIdResponse;
import com.example.demo.response.RequestResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.JwtService;
import com.example.demo.service.RequestService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final JwtService jwtService;

    @Override
    public RequestResponse insertLateSoonRequest(String userId, CreateSoonLateRequest createSoonLateRequest) {
        boolean userByEmail = userRepository.existsUserByEmail(createSoonLateRequest.getReceiverEmail());
        if (!userByEmail) {
            throw new CustomException(ResponseObject.MESSAGE_USER_NOT_FOUND_WITH_EMAIL + createSoonLateRequest.getReceiverEmail());
        }
        RequestChecker.checkCreateLateSoonRequest(createSoonLateRequest);
        Request request1 = requestRepository.findFirstByUserIdOrderByIdDesc(userId);
        Request requestInsert;
        if (request1 == null) {
            requestInsert = RequestConverter.convertRequestLateSoonToRequest(userId, createSoonLateRequest);
        } else {
            requestInsert = RequestConverter.convertRequestLateSoonToRequest(userId, createSoonLateRequest, request1.getDayRequest(), request1.getTimeRemainInWeek());
        }
        if (requestInsert.getTimeRemainInWeek() < 0) {
            throw new CustomException("MESSAGE NO REMAINING FOR CREATE LATESOON REQUEST");
        }
        Request insert = requestRepository.insert(requestInsert);
        RequestResponse response = RequestConverter.toResponseFromLate(insert, insert.getTimeRemainInWeek());
        return response;
    }

    @Override
    public RequestResponse insertLeaveRequest(String userId, CreateLeaveRequest createLeaveRequest) {
        boolean userByEmail = userRepository.existsUserByEmail(createLeaveRequest.getReceiverEmail());
        if (!userByEmail) {
            throw new CustomException("MESSAGE_USER_NOT_FOUND_WITH_EMAIL");
        }
        RequestChecker.checkCreateLeaveRequest(createLeaveRequest);
        Request requestInsert = RequestConverter.convertRequestLeaveToOffResponse(userId, createLeaveRequest);
        List<Request> requestList = requestRepository.findRequestsByYearRequestAndRequestTypeAndUserId(DateConvert.getCurrentYear(), RequestType.OFF, userId);
        int sum = requestList.stream().mapToInt(request -> (int) request.getNumDayOff()).sum();
        if (sum + requestInsert.getNumDayOff() > 180) {
            throw new CustomException("MESSAGE_NO_REMAINING_DAY_FOR_OFF_REQUEST");
        }
        Request insert = requestRepository.insert(requestInsert);
        RequestResponse response = RequestConverter.convertResponseFromOff(insert, 180 - requestInsert.getNumDayOff());
        return response;
    }

    @Override
    public List<RequestByUserIdResponse> findListRequestByUserId(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException("MESSAGE_USER_NOT_FOUND");
        }
        User user = optionalUser.get();
        List<RequestByUserIdResponse> collect = requestRepository.findRequestsByUserIdOrderByCreateTimeAsc(id)
                .stream()
                .map(request ->
                        RequestByUserIdResponse.builder()
                                .id(request.getId())
                                .status(request.getRequestStatus())
                                .requestTitle(request.getRequestTitle())
                                .message(request.getMessage())
                                .userName(user.getName())
                                .timeRequest(request.getDayRequest())
                                .timeNeed(RequestConverter.getTimeNeed(request))
                                .build())
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<RequestByUserIdResponse> findListRequestByReceiverEmail(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException("MESSAGE_USER_NOT_FOUND");
        }
        User user = optionalUser.get();
        List<RequestByUserIdResponse> collect = requestRepository.findRequestsByReceiverEmailAndRequestStatus(user.getEmail(), RequestStatus.PENDING)
                .stream()
                .map(request -> {
                    User requestCreator = userRepository.findUserById(request.getUserId());
                    return RequestByUserIdResponse.builder()
                            .id(request.getId())
                            .status(request.getRequestStatus())
                            .requestTitle(request.getRequestTitle())
                            .timeRequest(request.getDayRequest())
                            .message(request.getMessage())
                            .userName(requestCreator.getName())
                            .timeNeed(RequestConverter.getTimeNeed(request))
                            .build();
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public ListRequestResponse approveRequest(String requestId, String token, UpdateStatusRequest status) {
        JwtData data = jwtService.parseToken(token);
        String email = data.getEmail();
        Optional<Request> optionalRequest = requestRepository.findRequestsByIdAndReceiverEmail(requestId, email);
        if (optionalRequest.isEmpty()) {
            throw new CustomException("MESSAGE_REQUEST_NOT_FOUND");
        }
        Request request = optionalRequest.get();
        request.setRequestStatus(getStatusFromString(status));
        Request save = requestRepository.save(request);
        ListRequestResponse response = RequestConverter.toResponse(save);
        return response;
    }
    public static RequestStatus getStatusFromString(UpdateStatusRequest status) {
        if (RequestStatus.REJECTED.toString().equalsIgnoreCase(status.getStatus())) {
            return RequestStatus.REJECTED;
        }
        return RequestStatus.APPROVED;
    }
}
