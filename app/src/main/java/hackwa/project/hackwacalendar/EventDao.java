package hackwa.project.hackwacalendar;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
//import com.github.sundeepk.compactcalendarview.domain.Event;

@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Query("DELETE FROM event_table")
    void deleteAll();

    @Query("SELECT * from event_table ORDER BY id ASC")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * from event_table LIMIT 1")
    Event[] getAnyEvent();

    @Delete
    void deleteEvent(Event event);

    @Update
    void update(Event... event);
}
