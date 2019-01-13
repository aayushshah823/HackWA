package hackwa.project.hackwacalendar;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
//import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.List;


/**
 * Created by core on 16/12/18.
 */

public class EventViewModel extends AndroidViewModel {
    private EventRepository mRepository;
    private LiveData<List<Event>> mAllEvents;

    public EventViewModel (Application application) {
        super(application);
        mRepository = new EventRepository(application);
        mAllEvents = mRepository.getAllEvents();
    }

    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    public void insert(Event event) {
        mRepository.insert(event);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteEvent(Event event) {
        mRepository.deleteEvent(event);
    }

    public void update(Event event) {
        mRepository.update(event);
    }
}
