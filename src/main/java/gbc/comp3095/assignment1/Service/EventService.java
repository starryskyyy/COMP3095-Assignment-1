package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.Event;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }

    public Set<Event> getEvents(User user) {
        LocalDate today = LocalDate.now();
        LocalDate afterMonths = today.plusMonths(12);
        return eventRepository.findAllByUserAndDateBetweenOrderByDateAsc(user, today, afterMonths);
    }

    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }


}