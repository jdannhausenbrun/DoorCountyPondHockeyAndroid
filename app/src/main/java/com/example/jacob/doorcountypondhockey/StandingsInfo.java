package com.example.jacob.doorcountypondhockey;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Created by Jacob on 4/23/2017.
 */
public class StandingsInfo implements Comparable {
    private String teamName;
    private int wins = 0;
    private int losses = 0;
    private int pointsAgainst = 0;
    private HashMap<String, Integer> headToHeadScores = new HashMap<>();

    public StandingsInfo(String name) {
        teamName = name;
    }

    /**
     * Increments win record by 1
     */
    public void incrementWins() {
        wins++;
    }

    /**
     * Decrements win record by 1
     */
    public void decrementWins() {
        wins--;
    }

    /**
     * Increments loss record by 1
     */
    public void incrementLosses() {
        losses++;
    }

    /**
     * Decrements loss record by 1
     */
    public void decrementLosses() {
        losses--;
    }

    /**
     * Adds a given number of points to a team's total points against
     *
     * @param points the number of points
     */
    public void addPointsAgainst(int points) {
        pointsAgainst += points;
    }

    /**
     * Removes a given number of points from a team's total points against
     *
     * @param points the number of points
     */
    public void removePointsAgainst(int points) {
        pointsAgainst -= points;
    }

    /**
     * Adds a win to the head to head records with another team
     *
     * @param opposingTeam the name of the opposing team
     */
    public void addWinAgainst(String opposingTeam) {
        int current = 0;
        if (headToHeadScores.get(opposingTeam) != null) {
            current = headToHeadScores.get(opposingTeam);
        }
        headToHeadScores.put(opposingTeam, ++current);
    }

    /**
     * Removes a win from the head to head records with another team
     *
     * @param opposingTeam the name of the opposing team
     */
    public void removeWinAgainst(String opposingTeam) {
        int current = 0;
        if (headToHeadScores.get(opposingTeam) != null) {
            current = headToHeadScores.get(opposingTeam);
        }
        headToHeadScores.put(opposingTeam, --current);
    }

    /**
     * Adds a loss to the head to head records with another team
     *
     * @param opposingTeam the name of the opposing team
     */
    public void addLossAgainst(String opposingTeam) {
        int current = 0;
        if (headToHeadScores.get(opposingTeam) != null) {
            current = headToHeadScores.get(opposingTeam);
        }
        headToHeadScores.put(opposingTeam, --current);
    }

    /**
     * Removes a loss from the head to head records with another team
     *
     * @param opposingTeam the name of the opposing team
     */
    public void removeLossAgainst(String opposingTeam) {
        int current = 0;
        if (headToHeadScores.get(opposingTeam) != null) {
            current = headToHeadScores.get(opposingTeam);
        }
        headToHeadScores.put(opposingTeam, ++current);
    }

    public String getTeamName() {
        return teamName;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    /**
     * Calculates a teams win percentage
     *
     * @return a decimal value (<1) of the teams win percentage
     */
    public double getWinPercentage() {
        double totalGames = wins + losses;
        if (totalGames == 0) {
            return 0;
        }
        return ((double) wins) / (totalGames);
    }

    /**
     * Compares two teams to determine which team has a better standing
     *
     * @param obj the StandingsInfo object relating to the other team
     * @return integer representation of the ordering of the teams
     */
    @Override
    public int compareTo(@NonNull Object obj) {
        StandingsInfo otherInfo = (StandingsInfo) obj;
        double winPerc = this.getWinPercentage();
        double otherWinPerc = otherInfo.getWinPercentage();
        if (winPerc != otherWinPerc) {
            return (winPerc > otherWinPerc ? 1 : -1);
        }
        int headToHead = 0;
        if (this.headToHeadScores.get(otherInfo.teamName) != null) {
            headToHead = this.headToHeadScores.get(otherInfo.teamName);
        }
        if (headToHead != 0) {
            return headToHead;
        }
        return (this.pointsAgainst == otherInfo.pointsAgainst ? 0 :
                this.pointsAgainst > otherInfo.pointsAgainst ? -1 : 1);
    }
}
