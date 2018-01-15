package com.example.jacob.doorcountypondhockey;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jacob on 4/9/2017.
 */

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> implements Filterable {
    private ArrayList<Score> scores;
    private ArrayList<Score> filteredScores;

    public ScoresAdapter(ArrayList<Score> scores) {
        this.scores = scores;
        this.filteredScores = scores;
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
        final View scoreListItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.score_view, parent, false);
        return new ViewHolder(scoreListItem);
    }

    /**
     * Displays a ViewHolder at a given position in the RecyclerView.
     *
     * @param holder   the ViewHolder to be placed at a given position in the RecyclerView
     * @param position the position at which the ViewHolder will be placed
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Score score = filteredScores.get(position);
        holder.teamAName.setText(score.getTeamAName());
        holder.teamBName.setText(score.getTeamBName());
        //Sets the winning team's name and score text to bold
        if (score.getTeamAScore() > score.getTeamBScore()) {
            holder.teamAScore.setTypeface(Typeface.DEFAULT_BOLD);
            holder.teamAName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.teamBScore.setTypeface(Typeface.DEFAULT);
            holder.teamBName.setTypeface(Typeface.DEFAULT);
        } else {
            holder.teamBScore.setTypeface(Typeface.DEFAULT_BOLD);
            holder.teamBName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.teamAScore.setTypeface(Typeface.DEFAULT);
            holder.teamAName.setTypeface(Typeface.DEFAULT);
        }
        holder.teamAScore.setText(String.valueOf(score.getTeamAScore()));
        holder.teamBScore.setText(String.valueOf(score.getTeamBScore()));
    }

    /**
     * Used to determine when the RecyclerView should no longer permit scrolling
     *
     * @return the number of movies in the ArrayList
     */
    @Override
    public int getItemCount() {
        return filteredScores.size();
    }

    /**
     * Filters the scores list by the search query
     * (Scores can be searched using the names of the teams that played)
     *
     * @return a Filter containing the list of scores that match the search query
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString().toLowerCase();
                if (search.isEmpty()) {
                    filteredScores = scores;
                } else {
                    ArrayList<Score> tempFilteredScores = new ArrayList<>();
                    for (Score score : scores) {
                        String teamAName = score.getTeamAName().toLowerCase();
                        String teamBName = score.getTeamBName().toLowerCase();
                        if (teamAName.contains(search) || teamBName.contains(search)) {
                            tempFilteredScores.add(score);
                        }
                    }
                    filteredScores = tempFilteredScores;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredScores;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredScores = (ArrayList<Score>) filterResults.values;
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
        public TextView teamAScore;
        public TextView teamBScore;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            teamAName = (TextView) itemView.findViewById(R.id.teamANameView);
            teamBName = (TextView) itemView.findViewById(R.id.teamBNameView);
            teamAScore = (TextView) itemView.findViewById(R.id.teamAScoreView);
            teamBScore = (TextView) itemView.findViewById(R.id.teamBScoreView);
        }
    }
}
