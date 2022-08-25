package com.example.demo.repository;

import com.example.demo.constant.StatusEvent;
import com.example.demo.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findEventByStatusEvent(StatusEvent statusEvent);
}
