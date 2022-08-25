package com.example.demo.service.impl;

import com.example.demo.constant.StatusEvent;
import com.example.demo.converter.DateConvert;
import com.example.demo.converter.EventConverter;
import com.example.demo.domain.Event;
import com.example.demo.exception.EventNotFoundException;
import com.example.demo.exception.UserTypeNotAllow;
import com.example.demo.repository.EventRepository;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.request.event.UpdateEventRequest;
import com.example.demo.response.EventIncomingResponse;
import com.example.demo.response.EventResponse;
import com.example.demo.response.EventSearchResponse;
import com.example.demo.service.EventService;
import com.example.demo.service.JwtService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final JwtService jwtService;
    private final EventRepository eventRepository;

    @Override
    public EventResponse insert(String token, CreateEventRequest createEventRequest) throws Exception {
        JwtData jwtData = jwtService.parseToken(token);
        String userId = jwtData.getUserId();
        Event event = EventConverter.convertToEvent(userId, createEventRequest);
        Event insertedValue = eventRepository.insert(event);
        return EventConverter.convertToResponse(insertedValue);
    }

    @Override
    public Event save(UpdateEventRequest updateEventRequest, String eventId, String token) {
        Event event = eventRepository.findById(eventId).get();
        if (event == null) {
            throw new EventNotFoundException("Can't find Event with id = " + eventId);
        }
        JwtData jwtData = jwtService.parseToken(token);
        String userId = jwtData.getUserId();
        event.setContent(updateEventRequest.getContent());
        event.setTitle(updateEventRequest.getTitle());
        event.setUpdateUserId(userId);
        event.setLastUpdateTime(System.currentTimeMillis());
        event.setTimeBegin(DateConvert.fromStringToMillis(updateEventRequest.getTimeBegin()));
        event.setTimeEnd(DateConvert.fromStringToMillis(updateEventRequest.getTimeEnd()));
        event.setStatusEvent(DateConvert.checkEvent(event.getTimeBegin(),event.getTimeEnd()));
        return eventRepository.save(event);
    }

    @Override
    public EventResponse getEventById(String eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (event == null) {
            throw new EventNotFoundException("EVENT ID DOES NOT EXIST");
        }
        return EventConverter.convertToResponse(event);
    }

    @Override
    public List<EventSearchResponse> listEventByStatus(int page, int pageSize, StatusEvent statusEvent) {
        return eventRepository.findEventByStatusEvent(statusEvent)
                .stream()
                .skip((long) page * pageSize)
                .limit(pageSize)
                .map(event -> new EventSearchResponse(event.getId(), event.getTitle(),
                        DateConvert.convertLongToDate(event.getTimeBegin()), event.getStatusEvent()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getListEvent() {
        return eventRepository.findAll()
                .stream()
                .map(event ->
                        EventResponse.builder()
                                .id(event.getId())
                                .title(event.getTitle())
                                .banner(event.getBanner())
                                .content(event.getContent())
                                .statusEvent(event.getStatusEvent())
                                .timeBegin(DateConvert.convertLongToDate(event.getTimeBegin()))
                                .timeEnd(DateConvert.convertLongToDate(event.getTimeEnd()))
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEventById(String eventId, String token) {
        Event event = eventRepository.findById(eventId).get();
        if (event == null) {
            throw new EventNotFoundException("Can't delete eventId = " + eventId + " , it does not exit");
        }
        String userIdCreatEvent = jwtService.parseTokenToUserId(token);
        if (!userIdCreatEvent.equals(event.getCreateUserId())) {
            throw new UserTypeNotAllow("No permission to delete (Only the event creator can delete)");
        }
        eventRepository.deleteById(eventId);
    }

    @Override
    public List<EventIncomingResponse> getIncomingEvent() {
        return eventRepository.findEventByStatusEvent(StatusEvent.INCOMING)
                .stream()
                .sorted(Comparator.comparingLong(Event::getTimeBegin).reversed())
                .map(EventConverter::convertToIncomingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventSearchResponse> listEventInYear() {

        List<Event> eventList = eventRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Event::getTimeBegin).reversed())
                .collect(Collectors.toList());
        List<EventSearchResponse> eventInYear = new ArrayList<>();
        for (Event e : eventList) {
            if (DateConvert.checkInYear(e.getTimeBegin())) {
                eventInYear.add(EventConverter.convertToSearchResponse(e));
            }
        }
        return eventInYear;
    }


}
