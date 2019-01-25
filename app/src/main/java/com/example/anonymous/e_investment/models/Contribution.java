package com.example.anonymous.e_investment.models;

public class Contribution {
    String contribution_id,userId,group1d,amoount_transacted;
    public Contribution() {
    }

    public Contribution(String contribution_id, String userId, String group1d, String amoount_transacted) {
        this.contribution_id = contribution_id;
        this.userId = userId;
        this.group1d = group1d;
        this.amoount_transacted = amoount_transacted;
    }

    public String getContribution_id() {
        return contribution_id;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroup1d() {
        return group1d;
    }

    public String getAmoount_transacted() {
        return amoount_transacted;
    }
}
