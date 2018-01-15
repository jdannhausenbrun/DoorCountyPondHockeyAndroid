package com.example.jacob.doorcountypondhockey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Jacob on 4/18/2017.
 */

public class Standings {
    private HashMap<String, StandingsInfo> standings = new HashMap<>();

    public Standings() {
    }

    /**
     * Obtains the current standings based on the submitted scores
     *
     * @return a sorted list of StandingsInfo objects
     */
    public ArrayList<StandingsInfo> getStandings() {
        Collection<StandingsInfo> teams = standings.values();
        Comparator<StandingsInfo> teamComparator = new Comparator<StandingsInfo>() {
            @Override
            public int compare(StandingsInfo entry1, StandingsInfo entry2) {
                return (entry1).compareTo(entry2);
            }
        };
        //Converts map of teams to a list
        ArrayList<StandingsInfo> standings = new ArrayList<>(teams);
        //Sorts the new list according the StandingsInfo compareTo method
        Collections.sort(standings, teamComparator);
        return standings;
    }

    /**
     * Updates the information needed to calculate the standings when a game is added
     *
     * @param score the removed Score object pertaining the game
     */
    public void addGame(Score score) {
        String teamAName = score.getTeamAName();
        String teamBName = score.getTeamBName();
        int teamAScore = score.getTeamAScore();
        int teamBScore = score.getTeamBScore();
        StandingsInfo teamA = standings.get(teamAName);
        if (teamA == null) {
            standings.put(teamAName, new StandingsInfo(teamAName));
            teamA = standings.get(teamAName);
        }
        StandingsInfo teamB = standings.get(teamBName);
        if (teamB == null) {
            standings.put(teamBName, new StandingsInfo(teamBName));
            teamB = standings.get(teamBName);
        }
        //Updates head to head scores and overall record
        if (teamAScore > teamBScore) {
            teamA.incrementWins();
            teamA.addWinAgainst(teamBName);
            teamB.incrementLosses();
            teamB.addLossAgainst(teamAName);
        } else {
            teamA.incrementLosses();
            teamA.addLossAgainst(teamBName);
            teamB.incrementWins();
            teamB.addWinAgainst(teamAName);
        }
        //Updates total points against
        teamA.addPointsAgainst(teamBScore);
        teamB.addPointsAgainst(teamAScore);
    }

    /**
     * Updates the information needed to calculate the standings in the case that a game is removed
     *
     * @param score the removed Score object pertaining the game
     */
    public void removeGame(Score score) {
        String teamAName = score.getTeamAName();
        String teamBName = score.getTeamBName();
        int teamAScore = score.getTeamAScore();
        int teamBScore = score.getTeamBScore();
        StandingsInfo teamA = standings.get(teamAName);
        StandingsInfo teamB = standings.get(teamBName);
        //Updates head to head scores and overall record
        if (teamAScore > teamBScore) {
            teamA.decrementWins();
            teamA.removeWinAgainst(teamBName);
            teamB.decrementLosses();
            teamB.removeLossAgainst(teamAName);
        } else {
            teamA.decrementLosses();
            teamA.removeLossAgainst(teamBName);
            teamB.decrementWins();
            teamB.removeWinAgainst(teamAName);
        }
        //Updates total points against
        teamA.removePointsAgainst(teamBScore);
        teamB.removePointsAgainst(teamAScore);
    }

    public void changeGame(Score newScore, Score oldScore) {
        //Removes the outdated version of the game
        removeGame(oldScore);
        //Adds the edited version of the game
        addGame(newScore);
    }
}
