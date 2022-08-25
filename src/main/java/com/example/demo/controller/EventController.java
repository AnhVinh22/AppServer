package com.example.demo.controller;

import com.example.demo.annotation.RoleAdmin;
import com.example.demo.constant.StatusEvent;
import com.example.demo.domain.Event;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.request.event.UpdateEventRequest;
import com.example.demo.response.EventIncomingResponse;
import com.example.demo.response.EventResponse;
import com.example.demo.response.EventSearchResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("demo/v1/events/")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    public EventService eventService;

    @RoleAdmin
    @PostMapping()
    public ResponseEntity<ResponseObject> createEvent(@Valid @RequestHeader("Authorization") String token,
                                                      @RequestBody CreateEventRequest createEventRequest) throws Exception {
        EventResponse eventResponse = eventService.insert(token, createEventRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Create Event success", eventResponse));
    }

    @RoleAdmin
    @PutMapping("{eventId}")
    public ResponseEntity<ResponseObject> updateEvent(@PathVariable(name = "eventId") String eventId,
                                                      @Valid
                                                      @RequestBody UpdateEventRequest updateEventRequest,
                                                      @RequestHeader("Authorization") String token) {

        Event event = eventService.save(updateEventRequest, eventId, token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Update Event Success", event));
    }

    @RoleAdmin
    @DeleteMapping("{eventId}")
    public ResponseEntity<ResponseObject> deleteEventById(@PathVariable(name = "eventId") String eventId,
                                                          @RequestHeader("Authorization") String token) {
        eventService.deleteEventById(eventId, token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Delete Event Success", null));
    }

    @GetMapping("{eventId}")
    public ResponseEntity<ResponseObject> getEventById(@PathVariable(name = "eventId") String eventId) {
        EventResponse eventResponse = eventService.getEventById(eventId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Get Event By Id Success", eventResponse));
    }

    @GetMapping("incoming")
    public ResponseEntity<ResponseObject> getIncomingEvents() {
        List<EventIncomingResponse> result = eventService.getIncomingEvent();
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Find event with success", result));
    }

    @GetMapping("tree")
    public ResponseEntity<ResponseObject> getEventsInYear() {
        List<EventSearchResponse> result = eventService.listEventInYear();
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Find event with success", result));
    }

    @GetMapping()
    public ResponseEntity<ResponseObject> listByEventStatus(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "24") int pageSize,
                                                            @RequestParam(required = false) StatusEvent statusEvent) {
        if (statusEvent == null) {
            List<EventResponse> list = eventService.getListEvent();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject(HttpStatus.OK.value(),
                            "GET LIST EVENT", list));
        }
        List<EventSearchResponse> searchResponses = eventService.listEventByStatus(page, pageSize, statusEvent);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(),
                "GET LIST EVENT WITH STATUS" + statusEvent.name(), searchResponses));
    }
}
