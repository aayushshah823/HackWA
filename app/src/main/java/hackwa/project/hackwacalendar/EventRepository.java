package hackwa.project.hackwacalendar;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
//import com.github.sundeepk.compactcalendarview.domain.Event;
/**
 * A repository manages query threads that allows us to use multiple backends
 * implements the logic for deciding whether to fetch data from a network or use results
 * cached in the local database
 * handles data operations, provides a clean API to the rest of the app for app data
 * not part of the architecture components libraries but is suggested best practice for code
 * separation and architecture
 */

public class EventRepository {
    private EventDao mEventDao;
    private LiveData<List<Event>> mAllEvents;

    EventRepository(Application application) {
        EventRoomDatabase db = EventRoomDatabase.getDatabase(application);
        mEventDao = db.eventDao();
        mAllEvents = mEventDao.getAllEvents();
    }

    private static class insertAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;

        insertAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllEventsAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao mAsyncTaskDao;

        deleteAllEventsAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;

        deleteEventAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.deleteEvent(params[0]);
            return null;
        }
    }
    private static class updateEventAsynTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;

        updateEventAsynTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    public void insert(Event event) {
        new insertAsyncTask(mEventDao).execute(event);
    }

    public void deleteAll() {
        new deleteAllEventsAsyncTask(mEventDao).execute();
    }

    public void deleteEvent(Event event) {
        new deleteEventAsyncTask(mEventDao).execute(event);
    }

    public void update(Event event) {
        new updateEventAsynTask(mEventDao).execute(event);
    }
}