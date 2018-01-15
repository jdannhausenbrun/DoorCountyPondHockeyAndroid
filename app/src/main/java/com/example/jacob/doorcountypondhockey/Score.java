package com.example.jacob.doorcountypondhockey;

/**
 * Created by Jacob on 4/7/2017.
 */

public class Score {
    private String league;
    private String teamAName;
    private String teamBName;
    private int teamAScore;
    private int teamBScore;

    public Score() {
    }

    public Score(String league, String teamAName, String teamBName, int teamAScore, int teamBScore) {
        this.league = league;
        this.teamAName = teamAName;
        this.teamBName = teamBName;
        this.teamAScore = teamAScore;
        this.teamBScore = teamBScore;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

    public int getTeamAScore() {
        return teamAScore;
    }

    public void setTeamAScore(int teamAScore) {
        this.teamAScore = teamAScore;
    }

    public int getTeamBScore() {
        return teamBScore;
    }

    public void setTeamBScore(int teamBScore) {
        this.teamBScore = teamBScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Score)) {
            return false;
        }

        Score score = (Score) obj;

        if (teamAScore != score.teamAScore) {
            return false;
        }
        if (teamBScore != score.teamBScore) {
            return false;
        }
        if (!this.teamAName.equals(score.teamAName)) {
            return false;
        }
        if (!this.teamBName.equals(score.teamBName)) {
            return false;
        }
        if (!this.league.equals(score.league)) {
            return false;
        }
        return true;
    }
}
