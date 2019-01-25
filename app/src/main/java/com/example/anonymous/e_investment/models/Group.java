package com.example.anonymous.e_investment.models;

public class Group {
    String groupId,group_name,totalAmount,pic_url;

    public Group() {
    }

    public Group(String groupId, String group_name, String totalAmount, String pic_url) {
        this.groupId = groupId;
        this.group_name = group_name;
        this.totalAmount = totalAmount;
        this.pic_url = pic_url;
    }


    public String getGroupId() {
        return groupId;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getPic_url() {
        return pic_url;
    }
}
