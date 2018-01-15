package com.example.jacob.doorcountypondhockey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StandingsFragment extends Fragment {
    private RecyclerView recyclerView;
    private StandingsAdapter standingsAdapter;
    private ArrayList<StandingsInfo> standingsList = new ArrayList<>();
    private ArrayList<String> leagueList = new ArrayList<>();
    private ArrayList<String> leagueListKeys = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference leagueReference;
    private DatabaseReference scoresReference;
    private ArrayList<Score> scoreHistory = new ArrayList<>();
    private ArrayList<String> scoreKeys = new ArrayList<>();
    private Standings standings;

    public StandingsFragment() {
        // Required empty public constructor
    }

    public static StandingsFragment newInstance() {
        StandingsFragment fragment = new StandingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standings, container, false);
        final Spinner leagueSpinner = (Spinner) view.findViewById(R.id.leagueSpinner);

        //Sets the adapter for league selector
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, leagueList);
        leagueSpinner.setAdapter(spinnerAdapter);

        //Gets database reference for the leagues
        database = FirebaseDatabase.getInstance().getReference();
        leagueReference = database.child(getResources().getString(R.string.database_leagues_child));

        //Listens for changes in the leagues available in the database
        leagueReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                leagueList.clear();
                leagueListKeys.clear();
                for (DataSnapshot leagues : dataSnapshot.getChildren()) {
                    String leagueName = leagues.getValue(String.class);
                    leagueList.add(leagueName);
                    leagueListKeys.add(leagues.getKey());
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.leagues_retrieval_failure),
                        Toast.LENGTH_LONG).show();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.standingsRecyclerView);
        final LinearLayoutManager LINEAR_LAYOUT_MANAGER =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(LINEAR_LAYOUT_MANAGER);

        //Sets RecyclerView adapter
        standingsAdapter = new StandingsAdapter(standingsList);
        recyclerView.setAdapter(standingsAdapter);

        //Gets database reference for the scores
        scoresReference = database.child(getResources().getString(R.string.database_scores_child));

        leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String selectedLeagueKey =
                        leagueListKeys.get(leagueSpinner.getSelectedItemPosition());
                standings = new Standings();
                scoreHistory.clear();
                scoreKeys.clear();
                scoresReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Score score = dataSnapshot.getValue(Score.class);
                        if ((score.getLeague()).equals(selectedLeagueKey)) {
                            standings.addGame(score);
                            String scoreKey = dataSnapshot.getKey();
                            scoreHistory.add(score);
                            scoreKeys.add(scoreKey);
                            ArrayList<StandingsInfo> tempStandings = standings.getStandings();
                            //List must be reversed to be shown in correct order in the user interface
                            Collections.reverse(tempStandings);
                            standingsList.clear();
                            standingsList.addAll(tempStandings);
                            standingsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Score newScore = dataSnapshot.getValue(Score.class);
                        if ((newScore.getLeague()).equals(selectedLeagueKey)) {
                            String scoreKey = dataSnapshot.getKey();
                            int scoreIndex = scoreKeys.indexOf(scoreKey);
                            if (scoreIndex > -1) {
                                Score oldScore = scoreHistory.get(scoreIndex);
                                scoreHistory.set(scoreIndex, newScore);
                                standings.changeGame(newScore, oldScore);
                            }
                            ArrayList<StandingsInfo> tempStandings = standings.getStandings();
                            //List must be reversed to be shown in correct order in the user interface
                            Collections.reverse(tempStandings);
                            standingsList.clear();
                            standingsList.addAll(tempStandings);
                            standingsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Score score = dataSnapshot.getValue(Score.class);
                        if ((score.getLeague()).equals(selectedLeagueKey)) {
                            standings.removeGame(score);
                            String scoreKey = dataSnapshot.getKey();
                            scoreHistory.remove(score);
                            scoreKeys.remove(scoreKey);
                            ArrayList<StandingsInfo> tempStandings = standings.getStandings();
                            //List must be reversed to be shown in correct order in the user interface
                            Collections.reverse(tempStandings);
                            standingsList.clear();
                            standingsList.addAll(tempStandings);
                            standingsAdapter.notifyDataSetChanged();
                        }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //DO NOTHING
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


}
