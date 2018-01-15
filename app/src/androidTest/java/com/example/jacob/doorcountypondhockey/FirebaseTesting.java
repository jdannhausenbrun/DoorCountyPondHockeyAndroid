package com.example.jacob.doorcountypondhockey;

import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseTesting {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference testing = databaseReference.child("testing");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CountDownLatch writeSignal;

    @Before
    public void setUp() throws Exception {
        writeSignal = new CountDownLatch(1);
        if (mAuth.getCurrentUser() == null) {
            mAuth.signInWithEmailAndPassword("score1@gmail.com", "score1").
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            writeSignal.countDown();
                        }
                    });
        } else {
            writeSignal.countDown();
        }
        writeSignal.await(10, TimeUnit.SECONDS);
        assertEquals(0, writeSignal.getCount());
    }

    @Test
    public void testScoreWrite() throws InterruptedException {
        writeSignal = new CountDownLatch(1);
        DatabaseReference scores = testing.child("testScores");
        scores.child("testScore").setValue(new Score("legends", "team1", "team2", 2, 3)).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        writeSignal.countDown();
                    }
                });
        writeSignal.await(10, TimeUnit.SECONDS);
        assertEquals(0, writeSignal.getCount());
    }

    @Test
    public void testReadScore() throws InterruptedException {
        writeSignal = new CountDownLatch(1);
        DatabaseReference scores = testing.child("testScores");
        scores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Score expected = new Score("legends", "team1", "team2", 2, 3);
                Score actual = Iterables.get(dataSnapshot.getChildren(), 0).getValue(Score.class);
                assertEquals(expected, actual);
                writeSignal.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
        assertEquals(0, writeSignal.getCount());
    }

    @Test
    public void testReadLeague() throws InterruptedException {
        writeSignal = new CountDownLatch(1);
        DatabaseReference leagues = testing.child("testLeagues");
        leagues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String expected = "National";
                String actual = Iterables.get(dataSnapshot.getChildren(), 0).getValue(String.class);
                assertEquals(expected, actual);
                writeSignal.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
        assertEquals(0, writeSignal.getCount());
    }

    @Test
    public void testReadTeam() throws InterruptedException {
        writeSignal = new CountDownLatch(1);
        DatabaseReference teams = testing.child("testTeams").child("national");
        teams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String expected = "Test Team";
                String actual = Iterables.get(dataSnapshot.getChildren(), 0).getValue(String.class);
                assertEquals(expected, actual);
                writeSignal.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
        assertEquals(0, writeSignal.getCount());
    }

    @After
    public void tearDown() throws Exception {
        if (mAuth != null) {
            mAuth.signOut();
        }
    }
}
