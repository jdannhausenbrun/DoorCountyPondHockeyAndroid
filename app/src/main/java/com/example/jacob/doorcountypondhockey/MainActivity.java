package com.example.jacob.doorcountypondhockey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        mAuth = FirebaseAuth.getInstance();

        //Changes the visible fragment when a menu item is selected
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment updatedFragment = null;
                        user = mAuth.getCurrentUser();
                        switch (item.getItemId()) {
                            case R.id.navigation_score_entry:
                                if (user != null) {
                                    updatedFragment = ScoreEntryFragment.newInstance();
                                } else {
                                    updatedFragment = LoginFragment.newInstance();
                                }
                                break;
                            case R.id.navigation_scores:
                                updatedFragment = ScoresFragment.newInstance();
                                break;
                            case R.id.navigation_standings:
                                updatedFragment = StandingsFragment.newInstance();
                                break;
                            case R.id.navigation_schedule:
                                updatedFragment = ScheduleFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().
                                beginTransaction();
                        transaction.replace(R.id.content, updatedFragment);
                        transaction.commit();
                        return true;
                    }

                });

        //Set the default fragment to the login fragment when the application is started
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, LoginFragment.newInstance());
        transaction.commit();
    }
}
