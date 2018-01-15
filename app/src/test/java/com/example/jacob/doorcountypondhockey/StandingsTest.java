package com.example.jacob.doorcountypondhockey;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jacob on 5/2/2017.
 */
public class StandingsTest {
    private Standings standings;
    private Score scoreTeamOneWinVSTeamTwo = new Score("Test", "Team One", "Team Two", 6, 4);
    private Score scoreTeamTwoWinVSTeamOne = new Score("Test", "Team One", "Team Two", 5, 6);
    private Score scoreTeamFourWinVSTeamThree = new Score("Test", "Team Three", "Team Four", 5, 7);
    private Score scoreTeamThreeWinVSTeamOne = new Score("Test", "Team One", "Team Three", 5, 7);

    @Test
    public void testGetStandings() throws Exception {
        standings = new Standings();

        standings.addGame(scoreTeamOneWinVSTeamTwo);
        standings.addGame(scoreTeamFourWinVSTeamThree);

        ArrayList<StandingsInfo> standingsList = standings.getStandings();

        ArrayList<String> standingsExpected = new ArrayList<>();
        standingsExpected.add("Team Three");
        standingsExpected.add("Team Two");
        standingsExpected.add("Team Four");
        standingsExpected.add("Team One");

        for (int i = 0; i < standingsExpected.size() && i < standingsList.size(); i++) {
            String expectedTeam = standingsExpected.get(i);
            String actualTeam = standingsList.get(i).getTeamName();
            assertEquals(expectedTeam,actualTeam);
        }
    }

    @Test
    public void testAddGameWithNewTeams() throws Exception {
        standings = new Standings();

        standings.addGame(scoreTeamOneWinVSTeamTwo);
        int numTeamsOriginalActual = standings.getStandings().size();
        int numTeamsOriginalExpected = 2;
        assertEquals(numTeamsOriginalExpected, numTeamsOriginalActual);

        standings.addGame(scoreTeamFourWinVSTeamThree);
        int numTeamsFinalActual = standings.getStandings().size();
        int numTeamsFinalExpected = 4;
        assertEquals(numTeamsFinalExpected, numTeamsFinalActual);
    }

    @Test
    public void testAddGameWithRepeatTeam() throws Exception {
        standings = new Standings();

        standings.addGame(scoreTeamOneWinVSTeamTwo);
        int numTeamsOriginalActual = standings.getStandings().size();
        int numTeamsOriginalExpected = 2;
        assertEquals(numTeamsOriginalExpected, numTeamsOriginalActual);

        standings.addGame(scoreTeamThreeWinVSTeamOne);
        int numTeamsFinalActual = standings.getStandings().size();
        int numTeamsFinalExpected = 3;
        assertEquals(numTeamsFinalExpected, numTeamsFinalActual);
    }

    @Test
    public void testRemoveGame() throws Exception {
        standings = new Standings();

        standings.addGame(scoreTeamOneWinVSTeamTwo);
        standings.addGame(scoreTeamFourWinVSTeamThree);

        ArrayList<StandingsInfo> standingsList = standings.getStandings();

        for (StandingsInfo standingsInfo : standingsList) {
            if ( standingsInfo.getWins() == 0 && standingsInfo.getLosses() == 0 ) {
                throw new Exception();
            }
        }

        standings.removeGame(scoreTeamOneWinVSTeamTwo);
        standings.removeGame(scoreTeamFourWinVSTeamThree);

        standingsList = standings.getStandings();

        int numWinsExpected = 0;

        for (StandingsInfo standingsInfo : standingsList) {
            int numWinsActual = standingsInfo.getWins();
            assertEquals(numWinsExpected, numWinsActual);
        }
    }

    @Test
    public void testChangeGame() throws Exception {
        standings = new Standings();
        int winningTeamIndex = 1;

        standings.addGame(scoreTeamOneWinVSTeamTwo);
        String originalWinningTeamActual = standings.getStandings().
                get(winningTeamIndex).getTeamName();
        String originalWinningTeamExpected = "Team One";
        assertEquals(originalWinningTeamExpected,originalWinningTeamActual);

        standings.changeGame(scoreTeamTwoWinVSTeamOne,scoreTeamOneWinVSTeamTwo);
        String updatedWinningTeamActual = standings.getStandings().
                get(winningTeamIndex).getTeamName();
        String updatedWinningTeamExpected = "Team Two";
        assertEquals(updatedWinningTeamExpected,updatedWinningTeamActual);
    }

}