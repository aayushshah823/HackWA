package hackwa.project.hackwacalendar;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
//import com.github.sundeepk.compactcalendarview.domain.Event;

//singleton
@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class EventRoomDatabase extends RoomDatabase{

    public abstract EventDao eventDao();
    private static EventRoomDatabase INSTANCE;

    static EventRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (EventRoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventRoomDatabase.class, "event_database")
                            .fallbackToDestructiveMigration() // migration strategy: wipes and rebuilds instead of migrating if no Migration object
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }

        return INSTANCE;
    }

    // to delete all content and repopulate the database whenever the app is started
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    // populate the database in the background
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final EventDao mDao;
        String[] events = {"SMILE :) :)"};

        PopulateDbAsync(EventRoomDatabase db) {
            mDao = db.eventDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // start the app with clean database every time
            //mDao.deleteAll();

            /*if(mDao.getAnyEvent().length < 1) {
                for (int i = 0; i < events.length; i++) {
                    Event event = new Event(events[i]);
                    mDao.insert(event);
                }
            }*/
            return null;
        }
    }

}



