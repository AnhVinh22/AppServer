package com.example.demo.response;

import com.example.demo.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestByUserIdResponse {
    private String id;
    private String userName;
    private String requestTitle;
    private RequestStatus status;
    private String timeRequest;
    private String timeNeed;
    private String message;
}
