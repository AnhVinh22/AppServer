package com.example.demo.converter;

import com.example.demo.constant.StatusEvent;
import com.example.demo.domain.Event;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.response.EventIncomingResponse;
import com.example.demo.response.EventResponse;
import com.example.demo.response.EventSearchResponse;


public class EventConverter {

    public static EventResponse convertToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .banner(event.getBanner())
                .statusEvent(DateConvert.checkEvent(event.getTimeBegin(), event.getTimeEnd()))
                .content(event.getContent())
                .timeBegin(DateConvert.convertLongToDate(event.getTimeBegin()))
                .timeEnd(DateConvert.convertLongToDate(event.getTimeEnd()))
                .build();
    }


    public static EventIncomingResponse convertToIncomingResponse(Event event) {
        return EventIncomingResponse.builder()
                .title(event.getTitle())
                .banner(event.getBanner())
                .timeBegin(DateConvert.convertLongToDate(event.getTimeBegin()))
                .timeEnd(DateConvert.convertLongToDate(event.getTimeEnd()))
                .build();
    }

    public static EventSearchResponse convertToSearchResponse(Event event) {
        return EventSearchResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .statusEvent(DateConvert.checkEvent(event.getTimeBegin(), event.getTimeEnd()))
                .timeBegin(DateConvert.convertLongToDate(event.getTimeBegin()))
                .build();
    }

    public static Event convertToEvent(String userId, CreateEventRequest createEventRequest) throws Exception {
        long start = DateConvert.fromStringToMillis(createEventRequest.getTimeBegin());
        long end = DateConvert.fromStringToMillis(createEventRequest.getTimeEnd());
        StatusEvent status = DateConvert.checkEvent(start, end);
        return Event.builder()
                .title(createEventRequest.getTitle())
                .banner(createEventRequest.getBanner())
                .statusEvent(status)
                .content(createEventRequest.getContent())
                .timeBegin(DateConvert.fromStringToMillis(createEventRequest.getTimeBegin()))
                .timeEnd(DateConvert.fromStringToMillis(createEventRequest.getTimeEnd()))
                .createUserId(userId)
                .createTime(System.currentTimeMillis())
                .build();
    }


}
