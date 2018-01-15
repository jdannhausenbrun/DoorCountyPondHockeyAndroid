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

public class ScheduleFragment extends Fragment {
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private DatabaseReference database;
    private DatabaseReference schedule;
    private ArrayList<ScheduleEvent> scheduleList = new ArrayList<>();
    private ArrayList<String> scheduleListIds = new ArrayList<>();

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.scheduleRecyclerView);
        final LinearLayoutManager LINEAR_LAYOUT_MANAGER =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(LINEAR_LAYOUT_MANAGER);

        //Sets RecyclerView adapter
        scheduleAdapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(scheduleAdapter);

        //Gets database reference for the schedule
        database = FirebaseDatabase.getInstance().getReference();
        schedule = database.child("schedule");

        //Listens for changes in the events available in the database
        schedule.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ScheduleEvent scheduleEvent = dataSnapshot.getValue(ScheduleEvent.class);
                String scheduleEventKey = dataSnapshot.getKey();

                scheduleList.add(0, scheduleEvent);
                scheduleListIds.add(0, scheduleEventKey);

                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ScheduleEvent scheduleEvent = dataSnapshot.getValue(ScheduleEvent.class);
                String scheduleEventKey = dataSnapshot.getKey();

                int scoreIndex = scheduleListIds.indexOf(scheduleEventKey);
                if (scoreIndex > -1) {
                    scheduleList.set(scoreIndex, scheduleEvent);
                }

                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ScheduleEvent scheduleEvent = dataSnapshot.getValue(ScheduleEvent.class);
                String scheduleEventKey = dataSnapshot.getKey();
                scheduleList.remove(scheduleEvent);
                scheduleListIds.remove(scheduleEventKey);
                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //DO NOTHING
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.schedule_retrieval_failure),
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
        inflater.inflate(R.menu.schedule_search, menu);

        MenuItem search = menu.findItem(R.id.searchScheduleItem);
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
                if (scheduleAdapter != null) {
                    scheduleAdapter.getFilter().filter(newText);
                    scheduleAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }
}
