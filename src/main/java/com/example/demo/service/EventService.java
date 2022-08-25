package com.example.demo.service;

import com.example.demo.constant.StatusEvent;
import com.example.demo.domain.Event;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.request.event.UpdateEventRequest;
import com.example.demo.response.EventIncomingResponse;
import com.example.demo.response.EventResponse;
import com.example.demo.response.EventSearchResponse;

import java.util.List;

public interface EventService {
    EventResponse insert(String token, CreateEventRequest createEventRequest) throws Exception;

    Event save(UpdateEventRequest updateEventRequest, String eventId, String token);

    void deleteEventById(String eventId, String token);

    EventResponse getEventById(String eventId);

    List<EventSearchResponse> listEventByStatus(int page, int pageSize, StatusEvent statusEvent);

    List<EventResponse> getListEvent();

    List<EventIncomingResponse> getIncomingEvent();

    List<EventSearchResponse> listEventInYear();


}
