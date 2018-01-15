package com.example.jacob.doorcountypondhockey;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScoreEntryFragment extends Fragment {
    private ArrayList<String> leagueNamesList = new ArrayList<>();
    private ArrayList<String> leagueNameKeys = new ArrayList<>();
    private ArrayList<String> teamNamesList = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference scores;
    private DatabaseReference teams;
    private DatabaseReference leagues;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public ScoreEntryFragment() {
        // Required empty public constructor
    }

    public static ScoreEntryFragment newInstance() {
        ScoreEntryFragment fragment = new ScoreEntryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_entry, container, false);
        setHasOptionsMenu(true);
        final Spinner leagueSpinner = (Spinner) view.findViewById(R.id.leagueSpinnerEntry);
        final Spinner teamASpinner = (Spinner) view.findViewById(R.id.teamASpinnerEntry);
        final Spinner teamBSpinner = (Spinner) view.findViewById(R.id.teamBSpinnerEntry);
        final EditText teamAScoreView = (EditText) view.findViewById(R.id.teamAScoreEntry);
        final EditText teamBScoreView = (EditText) view.findViewById(R.id.teamBScoreEntry);
        final Button submitButton = (Button) view.findViewById(R.id.submitButton);

        database = FirebaseDatabase.getInstance().getReference();
        teams = database.child(getResources().getString(R.string.database_teams_child));
        leagues = database.child(getResources().getString(R.string.database_leagues_child));
        scores = database.child(getResources().getString(R.string.database_scores_child));

        final ArrayAdapter<String> leagueSpinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, leagueNamesList);
        leagueSpinner.setAdapter(leagueSpinnerAdapter);

        final ArrayAdapter<String> teamSpinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, teamNamesList);
        teamASpinner.setAdapter(teamSpinnerAdapter);
        teamBSpinner.setAdapter(teamSpinnerAdapter);

        //Loads the leagues into the spinner
        leagues.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                leagueNamesList.clear();
                leagueNameKeys.clear();
                for (DataSnapshot leagues : dataSnapshot.getChildren()) {
                    String leagueName = leagues.getValue(String.class);
                    String leagueKey = leagues.getKey();
                    leagueNamesList.add(leagueName);
                    leagueNameKeys.add(leagueKey);
                }
                leagueNamesList.add(0, getResources().getString(R.string.default_league_selection));
                leagueNameKeys.add(0, "");
                leagueSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.leagues_retrieval_failure),
                        Toast.LENGTH_LONG).show();
            }
        });

        //Loads a given league's teams into the spinners after a league is selected in the spinner
        leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       final int position, long l) {
                teams.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        teamNamesList.clear();
                        for (DataSnapshot leagues : dataSnapshot.getChildren()) {
                            if ((leagues.getKey()).equals((leagueNameKeys.get(position)))) {
                                for (DataSnapshot team : leagues.getChildren()) {
                                    String teamName = team.getValue(String.class);
                                    teamNamesList.add(teamName);
                                }
                                break;
                            }
                        }
                        teamSpinnerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.teams_retrieval_failure),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //DO NOTHING
            }
        });

        //Listens for the user to sign out in which case it switches to the login fragment
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, LoginFragment.newInstance());
                    transaction.commit();
                }
            }
        };

        //Submits a score to the database when the button is clicked on and the input is valid
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Gets score info from the text views and spinners
                String teamAScoreString = teamAScoreView.getText().toString();
                String teamBScoreString = teamBScoreView.getText().toString();

                int teamAScore = 0;
                int teamBScore = 0;

                if (teamAScoreString.length() != 0) {
                    teamAScore = Integer.parseInt(teamAScoreString);
                }
                if (teamBScoreString.length() != 0) {
                    teamBScore = Integer.parseInt(teamBScoreString);
                }

                Object teamANameItem = teamASpinner.getSelectedItem();
                String teamAName = teamANameItem == null ? "" : teamANameItem.toString();
                Object teamBNameItem = teamBSpinner.getSelectedItem();
                String teamBName = teamBNameItem == null ? "" : teamBNameItem.toString();
                String leagueName = leagueNameKeys.get(leagueSpinner.getSelectedItemPosition());

                if (teamAScoreString.length() == 0 || teamBScoreString.length() == 0 ||
                        teamAName.length() == 0 || teamBName.length() == 0) {
                    //Shows an alert dialog if a field is left empty
                    AlertDialog.Builder emptyFieldsBuilder = new AlertDialog.Builder(getContext());
                    emptyFieldsBuilder.setMessage(getString(R.string.missing_fields_error));
                    emptyFieldsBuilder.setCancelable(true);

                    emptyFieldsBuilder.setPositiveButton(getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog emptyFieldsAlert = emptyFieldsBuilder.create();
                    emptyFieldsAlert.show();
                } else if (!ScoreEntryFragment.checkValidInput(teamAScore, teamBScore,
                        teamAName, teamBName)) {
                    //Shows an alert dialog if the input is not valid
                    AlertDialog.Builder emptyFieldsBuilder = new AlertDialog.Builder(getContext());
                    emptyFieldsBuilder.setMessage(getString(R.string.matching_entries_error));
                    emptyFieldsBuilder.setCancelable(true);

                    emptyFieldsBuilder.setPositiveButton(getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog emptyFieldsAlert = emptyFieldsBuilder.create();
                    emptyFieldsAlert.show();
                } else {
                    //Pushes score to the database and shows an alert dialog containing
                    //the score information
                    scores.push().setValue(new Score(leagueName, teamAName, teamBName,
                            teamAScore, teamBScore));

                    AlertDialog.Builder submitSuccessBuilder = new AlertDialog.Builder(getContext());
                    submitSuccessBuilder.setMessage(getString(R.string.score_submitted) +
                            "\n" + teamAName + " - " + teamAScore + " vs. " +
                            "\n" + teamBName + " - " + teamBScore);
                    submitSuccessBuilder.setCancelable(true);

                    submitSuccessBuilder.setPositiveButton(getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog submitSuccessAlert = submitSuccessBuilder.create();
                    submitSuccessAlert.show();

                    teamAScoreView.setText("");
                    teamBScoreView.setText("");
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Determines if the score input is valid. To be valid, the game must not end in a tie and the
     * team names cannot be the same
     *
     * @param teamAScore the number of goals that team A scored
     * @param teamBScore the number of goals that team B scored
     * @param teamAName  team A's name
     * @param teamBName  team B's name
     * @return true if the names and scores differ, false otherwise
     */
    public static boolean checkValidInput(int teamAScore, int teamBScore, String teamAName,
                                          String teamBName) {
        if (teamAScore == teamBScore) {
            return false;
        }
        if (teamAName.equals(teamBName)) {
            return false;
        }
        return true;
    }

    /**
     * Adds Firebase authentication listener
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Removes Firebase authentication listener
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Creates the logout menu in the application's header
     *
     * @param menu the menu in which the settings are placed
     * @return this method must return true for the menu to be displayed
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }

    /**
     * Called whenever a menu item is selected
     *
     * @param item the selected item menu
     * @return true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_sign_out:
                mAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
