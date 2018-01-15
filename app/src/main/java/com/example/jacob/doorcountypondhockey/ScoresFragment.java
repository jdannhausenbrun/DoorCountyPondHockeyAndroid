package com.example.jacob.doorcountypondhockey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ScoresFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Score> scoresList = new ArrayList<>();
    private ArrayList<String> scoresListIDs = new ArrayList<>();
    private ScoresAdapter scoresAdapter;
    private DatabaseReference database;
    private DatabaseReference scores;

    public ScoresFragment() {
        // Required empty public constructor
    }

    public static ScoresFragment newInstance() {
        ScoresFragment fragment = new ScoresFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.scoresRecylcerView);
        final LinearLayoutManager LINEAR_LAYOUT_MANAGER =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(LINEAR_LAYOUT_MANAGER);

        //Sets RecyclerView adapter
        scoresAdapter = new ScoresAdapter(scoresList);
        recyclerView.setAdapter(scoresAdapter);

        //Gets database reference for the scores
        database = FirebaseDatabase.getInstance().getReference();
        scores = database.child(getResources().getString(R.string.database_scores_child));

        //Listens for changes in the scores available in the database
        scores.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Score score = dataSnapshot.getValue(Score.class);
                String scoreKey = dataSnapshot.getKey();

                scoresList.add(0, score);
                scoresListIDs.add(0, scoreKey);

                scoresAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Score score = dataSnapshot.getValue(Score.class);
                String scoreKey = dataSnapshot.getKey();

                int scoreIndex = scoresListIDs.indexOf(scoreKey);
                if (scoreIndex > -1) {
                    scoresList.set(scoreIndex, score);
                }

                scoresAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Score score = dataSnapshot.getValue(Score.class);
                String scoreKey = dataSnapshot.getKey();
                scoresList.remove(score);
                scoresListIDs.remove(scoreKey);
                scoresAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //DO NOTHING
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.scores_retrieval_failure),
                        Toast.LENGTH_LONG).show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Creates the score_search menu in the application's header
     *
     * @param menu the menu in which the settings are placed
     * @return this method must return true for the menu to be displayed
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.score_search, menu);

        MenuItem search = menu.findItem(R.id.searchScoreItem);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
    }

    /**
     * Searches the adapter for views that match the query
     *
     * @param searchView the SearchView that contains the query
     */
    public void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (scoresAdapter != null) {
                    scoresAdapter.getFilter().filter(newText);
                    scoresAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }
}
