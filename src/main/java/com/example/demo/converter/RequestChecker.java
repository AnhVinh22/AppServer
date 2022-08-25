package com.example.demo.converter;

import com.example.demo.exception.CustomException;
import com.example.demo.request.Request.CreateLeaveRequest;
import com.example.demo.request.Request.CreateSoonLateRequest;
import com.example.demo.response.ResponseObject;

public class RequestChecker {
    public static void checkCreateLateSoonRequest(CreateSoonLateRequest request){
        if (request.getLateOrSoon() == null){
            throw new CustomException(ResponseObject.MESSAGE_NULL_LATE_SOON_TYPE);
        }
        if (request.getShift() == null){
            throw new CustomException(ResponseObject.MESSAGE_NULL_SHIFT);
        }
        if (request.getReasons() == null){
            throw new CustomException(ResponseObject.MESSAGE_NULL_REASON);
        }
    }
    public static void checkCreateLeaveRequest(CreateLeaveRequest request){
        if (request.getReasons() == null){
            throw new CustomException(ResponseObject.MESSAGE_NULL_REASON);
        }
        if (request.getShiftStart() == null  || request.getShiftEnd() == null){
            throw new CustomException(ResponseObject.MESSAGE_NULL_SHIFT);
        }
        if (request.getOffType() == null){
            throw new CustomException(ResponseObject.MESSAGE_NULL_OFF_TYPE);
        }
    }
}
