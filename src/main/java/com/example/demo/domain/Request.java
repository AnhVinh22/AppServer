package com.example.demo.domain;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class Request {
    @Id
    private String id;
    private String userId;
    private String requestTitle;
    private String message;
    private String dayRequest; // AFTERNOON - 28/7/2022
    private double numDayOff;
    private String timeNeed;
    private int yearRequest;
    private long createTime;
    private RequestType requestType;
    private RequestStatus requestStatus;
    private String receiverEmail;

    //
    private int timeRemainInWeek;
}
