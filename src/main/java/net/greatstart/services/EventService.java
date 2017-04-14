package net.greatstart.services;

import net.greatstart.dao.EventDao;
import net.greatstart.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventDao eventDao;

    public boolean createEvent(Event event) {
        return eventDao.create(event);
    }

    public boolean updateEvent(Event event) {
        return eventDao.update(event);
    }

    public boolean deleteEvent(Event event) {
        return eventDao.delete(event);
    }

    public Event getEventById(long id) {
        return eventDao.getById(id);
    }

    public List<Event> getAllEvents() {
        return eventDao.getAll();
    }
}
