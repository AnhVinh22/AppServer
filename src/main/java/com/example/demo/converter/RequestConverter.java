package com.example.demo.converter;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import com.example.demo.domain.Request;
import com.example.demo.request.Request.CreateLeaveRequest;
import com.example.demo.request.Request.CreateSoonLateRequest;
import com.example.demo.response.ListRequestResponse;
import com.example.demo.response.RequestResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Component
public class RequestConverter {

    private static ModelMapper modelMapper;
    @Autowired
    private ModelMapper modelMapperObj;

    @PostConstruct
    private  void postConstruct() {
        modelMapper = this.modelMapperObj;
    }

    public static Request convertRequestLeaveToOffResponse(String userId, CreateLeaveRequest createLeaveRequest) {
        return Request.builder()
                .requestStatus(RequestStatus.PENDING)
                .requestTitle(createLeaveRequest.getOffType().toString())
                .receiverEmail(createLeaveRequest.getReceiverEmail())
                .createTime(System.currentTimeMillis())
                .message(createLeaveRequest.getMessage())
                .dayRequest(createLeaveRequest.getDateBegin() + " - " + createLeaveRequest.getDateEnd())
                .numDayOff(DateConvert.calculateDayOff(createLeaveRequest))
                .requestType(RequestType.OFF)
                .userId(userId)
                .yearRequest(DateConvert.getCurrentYear())
                .build();
    }

    public static RequestResponse convertResponseFromOff(Request request, double remain) {
        return RequestResponse.builder()
                .id(request.getId())
                .requestTitle(request.getRequestTitle())
                .requestStatus(request.getRequestStatus())
                .timeRequest(request.getDayRequest())
                .timeNeed(RequestConverter.getTimeNeed(request))
                .timeRemain(remain + "days in this year")
                .build();
    }

    public static Request convertRequestLateSoonToRequest(String userId, CreateSoonLateRequest createSoonLateRequest) {
        return Request.builder()
                .requestStatus(RequestStatus.PENDING)
                .requestTitle(createSoonLateRequest.getLateOrSoon().toString())
                .receiverEmail(createSoonLateRequest.getReceiverEmail())
                .createTime(System.currentTimeMillis())
                .message(createSoonLateRequest.getMessage())
                .dayRequest(createSoonLateRequest.getShift().toString() + " - " + createSoonLateRequest.getDayRequest())
                .timeNeed(createSoonLateRequest.getTimeLateOrSoon())
                .requestType(RequestType.LATE_SOON)
                .userId(userId)
                .timeRemainInWeek(1)
                .yearRequest(DateConvert.getCurrentYear())
                .build();
    }

    public static Request convertRequestLateSoonToRequest(String userId, CreateSoonLateRequest createSoonLateRequest,
                                                          String lastRequestDay, int oldRemain) {
        return Request.builder()
                .requestStatus(RequestStatus.PENDING)
                .requestTitle(createSoonLateRequest.getLateOrSoon().toString())
                .receiverEmail(createSoonLateRequest.getReceiverEmail())
                .createTime(System.currentTimeMillis())
                .message(createSoonLateRequest.getMessage())
                .dayRequest(createSoonLateRequest.getShift().toString() + " - " + createSoonLateRequest.getDayRequest())
                .timeNeed(createSoonLateRequest.getTimeLateOrSoon())
                .requestType(RequestType.LATE_SOON)
                .userId(userId)
                .timeRemainInWeek(countTimeRemaining(createSoonLateRequest.getDayRequest(), lastRequestDay, oldRemain))
                .yearRequest(DateConvert.getCurrentYear())
                .build();
    }

    public static int countTimeRemaining(String dayNewRequest, String dayOldRequest, int oldRemain) {
        // TODO - M???c ????ch: convert 2 ng??y xem c??i tu???n c???a ng??y request m???i, v?? ng??y request ngh??? c?? c?? tr??ng nhau hay kh??ng?
        //  N???u tr??ng nhau, t???c l?? trong 1 tu???n ?????ng ch?? n??y ngh??? 2 ng??y => tr??? v??? 0 (ZERO). Ng?????c l???i, 2 tu???n kh??c nhau,
        //  t???c l?? ?????ng ch?? n??y ngh??? 2 ng??y kh??c nhau (kh??ng c??ng trong 1 tu???n) => tr??? v??? 1.

        // TODO: Split date part from dayOldRequest
        //  dayOldRequest: AFTERNOON - 28/7/2022 (this value is invalid datetime format)
        String[] split = dayOldRequest.split(" - ");
        long millisNew = DateConvert.fromStringToMillis(dayNewRequest);
        long millisOld = DateConvert.fromStringToMillis(split[1]);

        Calendar calendarNew = Calendar.getInstance();
        calendarNew.setTimeInMillis(millisNew);
        Calendar calendarOld = Calendar.getInstance();
        calendarOld.setTimeInMillis(millisOld);

        int week1 = calendarNew.get(Calendar.WEEK_OF_YEAR);
        int week2 = calendarOld.get(Calendar.WEEK_OF_YEAR);
        if (week1 == week2) {
            return oldRemain - 1;
        }
        return 1;
    }

    public static String getTimeNeed(Request request) {
        if (request.getRequestType().equals(RequestType.OFF)) {
            return request.getNumDayOff() + " ng??y ";
        }
        return request.getTimeNeed();
    }

    public static RequestResponse toResponseFromLate(Request request, int remain) {
//        return RequestResponse.builder()
//                .id(request.getId())
//                .requestTitle(request.getRequestTitle())
//                .requestStatus(request.getRequestStatus())
//                .timeRequest(request.getDayRequest())
//                .timeNeed(RequestConverter.getTimeNeed(request))
//                .build();

        RequestResponse response = modelMapper.map(request, RequestResponse.class);
        response.setTimeNeed(getTimeNeed(request));
        response.setTimeRemain(remain + " time in week");
        return response;
    }

    public static ListRequestResponse toResponse(Request request) {
        return ListRequestResponse.builder()
                .id(request.getId())
                .requestTitle(request.getRequestTitle())
                .requestStatus(request.getRequestStatus())
                .timeRequest(request.getDayRequest())
                .timeNeed(RequestConverter.getTimeNeed(request))
                .build();
    }
}
