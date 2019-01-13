package hackwa.project.hackwacalendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * The adapter caches data and populates the RecyclerView with it.
 * The inner class WordViewHolder holds and manages a view for one list item.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {
    private final LayoutInflater mInflater;
    private List<Event> mEvents; // Cached copy of words
    private static ClickListener clickListener;

    EventListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    // holds and manages a view for one list item
    class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventItemView;

        private EventViewHolder(View itemView) {
            super(itemView);
            eventItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        if(mEvents != null) {
            Event current = mEvents.get(position);
            holder.eventItemView.setText(current.getEvent());
        }
        else {
            // covers the case of data not being ready yet
            holder.eventItemView.setText("No Word");
        }
    }

    void setEvents(List<Event> events) {
        mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mEvents != null) {
            return mEvents.size();
        }
        else {
            return 0;
        }
    }

    public Event getEventAtPosition(int position) {
        return mEvents.get(position);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        EventListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}