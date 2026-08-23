package com.tinoziko.the_real_dor.the_real_dor;

public class PlayerStats {
    private String name;
    private String position;
    private float totalMinutes;
    private float goals;
    private float assists;
    private float goalsPer90;
    private float assistsPer90;
    private float expectedGoals;
    private float scaPer90;
    private float keyPassesPer90;
    private float interceptionsPer90;
    private float tacklesWonPer90;
    private float progPassesPer90;
    private float progCarriesPer90;
    private double aiScore;

    public PlayerStats(String name, String position, float totalMinutes, float goals, float assists, float goalsPer90, float assistsPer90, float expectedGoals, float scaPer90, float keyPassesPer90, float interceptionsPer90, float tacklesWonPer90, float progPassesPer90, float progCarriesPer90) {
        this.name = name;
        this.position = position;
        this.totalMinutes = totalMinutes;
        this.goals = goals;
        this.assists = assists;
        this.goalsPer90 = goalsPer90;
        this.assistsPer90 = assistsPer90;
        this.expectedGoals = expectedGoals;
        this.scaPer90 = scaPer90;
        this.keyPassesPer90 = keyPassesPer90;
        this.interceptionsPer90 = interceptionsPer90;
        this.tacklesWonPer90 = tacklesWonPer90;
        this.progPassesPer90 = progPassesPer90;
        this.progCarriesPer90 = progCarriesPer90;
    }


    public String getName() { return name; }
    public String getPosition() { return position; }
    public float getTotalMinutes() { return totalMinutes; }
    public float getGoals() { return goals; }
    public float getAssists() { return assists; }
    public float getGoalsPer90() { return goalsPer90; }
    public float getAssistsPer90() { return assistsPer90; }
    public float getExpectedGoals() { return expectedGoals; }
    public float getScaPer90() { return scaPer90; }
    public float getKeyPassesPer90() { return keyPassesPer90; }
    public float getInterceptionsPer90() { return interceptionsPer90; }
    public float getTacklesWonPer90() { return tacklesWonPer90; }
    public float getProgPassesPer90() { return progPassesPer90; }
    public float getProgCarriesPer90() { return progCarriesPer90; }

    public double getAiScore() { return aiScore; }
    public void setAiScore(double aiScore) { this.aiScore = aiScore; }
}