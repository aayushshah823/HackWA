package hackwa.project.hackwacalendar;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.EventLog;
import android.view.View;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
//import com.github.sundeepk.compactcalendarview.domain.Event;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;

// FAB
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Locale;

import android.util.EventLog.*;
import android.support.design.widget.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private EventViewModel mEventViewModel;

    public static final int NEW_EVENT_REQUEST_CODE = 1;
    public static final int UPDATE_EVENT_ACTIVITY_REQUEST_CODE = 2;


    public static final String EXTRA_DATA_UPDATE_EVENT = "hackwa.project.hackwacalendar.EXTRA_DATA_UPDATE_EVENT";
    public static final String EXTRA_DATA_ID = "hackwa.project.hackwacalendar.EXTRA_DATA_ID";

    CompactCalendarView compactCalendarView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-DD-YYYY", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  final ActionBar actionBar = getSupportActionBar();
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getSupportActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setTitle(null);

        }


        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //Event evl = new Event(Color.RED, 1547339299L, "Teachers' Professional Day");
        //compactCalendarView.addEvent(evl);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                if(dateClicked.toString().compareTo("Fri Oct 21 09:00:00 AST 2019") == 0) {
                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No Events Planned", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if(getSupportActionBar() != null) {
                    getActionBar().setDisplayHomeAsUpEnabled(true);
//                    getActionBar().setTitle(null);
                    getActionBar().setTitle(simpleDateFormat.format(firstDayOfNewMonth));

                }
//                    getSupportActionBar().setTitle(simpleDateFormat.format(firstDayOfNewMonth));
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                startActivityForResult(intent, NEW_EVENT_REQUEST_CODE);
            }
        });

        // add RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final EventListAdapter adapter = new EventListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get ViewModel
        mEventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        // add observer for the LiveData returned by getAllEvents
        mEventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            // when the observed data changes while the activity is in foreground
            // onChanged() is invoked and updates the data cached in the adapter
            @Override
            public void onChanged(@Nullable final List<Event> events) {
                //update the cached copy of the todos in the adapter
                adapter.setEvents(events);
            }
        });

        // add functionality to swipe items in the recycler view to delete item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    // delete todo when swiped
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        // gets position of the viewholder that was swiped
                        int position = viewHolder.getAdapterPosition();

                        // based on position, get the todo displayed by the view holder
                        Event myEvent = adapter.getEventAtPosition(position);
                        Toast.makeText(MainActivity.this, "Deleting " +
                                myEvent.getEvent(), Toast.LENGTH_LONG).show();

                        mEventViewModel.deleteEvent(myEvent);
                    }
                });

        // attached the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new EventListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Event event = adapter.getEventAtPosition(position);
                //Toast.makeText(getBaseContext(), "item clicked", Toast.LENGTH_LONG).show();
                launchUpdateEventActivity(event);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Long tsLong = System.currentTimeMillis();
        if(requestCode == NEW_EVENT_REQUEST_CODE && resultCode == RESULT_OK){

            Event event = new Event(Color.RED, tsLong, data.getStringExtra(NewEventActivity.EXTRA_REPLY));
            mEventViewModel.insert(event);
        }
        else if (requestCode == UPDATE_EVENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String event_data = data.getStringExtra(NewEventActivity.EXTRA_REPLY);
            int id = data.getIntExtra(NewEventActivity.EXTRA_REPLY_ID, -1);

            if(id != -1) {
                mEventViewModel.update(new Event(id, Color.RED, tsLong,  event_data));
            }
            else {
                Toast.makeText(this, R.string.unable_to_update, Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    public void launchUpdateEventActivity(Event event) {
        Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_EVENT, event.getEvent());
        intent.putExtra(EXTRA_DATA_ID, event.getId());
        startActivityForResult(intent, UPDATE_EVENT_ACTIVITY_REQUEST_CODE);
    }

}
