package com.example.jacob.doorcountypondhockey;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jacob on 4/23/2017.
 */

public class StandingsAdapter extends RecyclerView.Adapter<StandingsAdapter.ViewHolder> {
    ArrayList<StandingsInfo> standings;

    public StandingsAdapter(ArrayList<StandingsInfo> standings) {
        this.standings = standings;
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
        final View standingsListItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.standing_view, parent, false);
        return new ViewHolder(standingsListItem);
    }

    /**
     * Displays a ViewHolder at a given position in the RecyclerView.
     *
     * @param holder   the ViewHolder to be placed at a given position in the RecyclerView
     * @param position the position at which the ViewHolder will be placed
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StandingsInfo standing = standings.get(position);
        holder.ranking.setText(String.valueOf(position + 1) + ".");
        holder.teamName.setText(standing.getTeamName());
        holder.numWins.setText(String.valueOf(standing.getWins()));
        holder.numLosses.setText(String.valueOf(standing.getLosses()));
    }


    /**
     * Used to determine when the RecyclerView should no longer permit scrolling
     *
     * @return the number of movies in the ArrayList
     */
    @Override
    public int getItemCount() {
        return standings.size();
    }

    /**
     * Remembers the subviews of the RecyclerView so that they do not need to be recreated each
     * time a view reappears on the screen
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView ranking;
        public TextView teamName;
        public TextView numWins;
        public TextView numLosses;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ranking = (TextView) itemView.findViewById(R.id.rankingView);
            teamName = (TextView) itemView.findViewById(R.id.teamNameView);
            numWins = (TextView) itemView.findViewById(R.id.numWinsView);
            numLosses = (TextView) itemView.findViewById(R.id.numLossesView);
        }
    }
}
