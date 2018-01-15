package com.example.jacob.doorcountypondhockey;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jacob on 4/24/2017.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private String rinkSearchStringTypeOne = "rink ";
    private String rinkSearchStringTypeTwo = "rink: ";
    private ArrayList<ScheduleEvent> schedule;
    private ArrayList<ScheduleEvent> filteredSchedule;

    public ScheduleAdapter(ArrayList<ScheduleEvent> schedule) {
        this.schedule = schedule;
        this.filteredSchedule = schedule;
    }

    /**
     * Creates a new ViewHolder to represent an item
     *
     * @param parent   the view group that the view will be added to after being bound to a position
     * @param viewType the type of view to be created
     * @return a ViewHolder containing a View of type viewType
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View scheduleEventListItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.schedule_event_view, parent, false);
        return new ViewHolder(scheduleEventListItem);
    }

    /**
     * Displays a ViewHolder at a given position in the RecyclerView.
     *
     * @param holder   the ViewHolder to be placed at a given position in the RecyclerView
     * @param position the position at which the ViewHolder will be placed
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ScheduleEvent scheduleEvent = filteredSchedule.get(position);
        holder.teamAName.setText(scheduleEvent.getTeamA());
        holder.teamBName.setText(scheduleEvent.getTeamB());
        holder.gameTime.setText(scheduleEvent.getGameTime());
        holder.rinkNumber.setText("Rink: " + String.valueOf(scheduleEvent.getRinkNumber()));
    }

    /**
     * Used to determine when the RecyclerView should no longer permit scrolling
     *
     * @return the number of movies in the ArrayList
     */
    @Override
    public int getItemCount() {
        return filteredSchedule.size();
    }

    /**
     * Filters the schedule list by the search query
     * (Events can be searched using the names of the teams that are playing and by the rink number)
     *
     * @return a Filter containing the list of events that match the search query
     */
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString().toLowerCase();
                if (search.isEmpty()) {
                    filteredSchedule = schedule;
                } else {
                    ArrayList<ScheduleEvent> tempFilteredSchedule = new ArrayList<>();
                    for (ScheduleEvent scheduleEvent : schedule) {
                        String teamAName = scheduleEvent.getTeamA().toLowerCase();
                        String teamBName = scheduleEvent.getTeamB().toLowerCase();
                        String rinkNumber = String.valueOf(scheduleEvent.getRinkNumber());
                        String rinkSearch = rinkSearchStringTypeOne + rinkNumber +
                                rinkSearchStringTypeTwo + rinkNumber;
                        if (teamAName.contains(search) || teamBName.contains(search) ||
                                rinkSearch.contains(search)) {
                            tempFilteredSchedule.add(scheduleEvent);
                        }
                    }
                    filteredSchedule = tempFilteredSchedule;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSchedule;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredSchedule = (ArrayList<ScheduleEvent>) filterResults.values;
            }
        };
    }

    /**
     * Remembers the subviews of the RecyclerView so that they do not need to be recreated each
     * time a view reappears on the screen
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView teamAName;
        public TextView teamBName;
        public TextView gameTime;
        public TextView rinkNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            teamAName = (TextView) itemView.findViewById(R.id.teamAScheduleView);
            teamBName = (TextView) itemView.findViewById(R.id.teamBScheduleView);
            gameTime = (TextView) itemView.findViewById(R.id.gameTimeView);
            rinkNumber = (TextView) itemView.findViewById(R.id.rinkNumberView);
        }
    }
}

