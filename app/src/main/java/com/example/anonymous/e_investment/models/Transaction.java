package com.example.anonymous.e_investment.models;

public class Transaction {
    String transaction_id,userid,date_transacted,groupId,amount_transacted;

    public Transaction() {
    }

    public Transaction(String transaction_id, String userid, String date_transacted, String groupId ,String amount_transacted) {
        this.transaction_id = transaction_id;
        this.userid = userid;
        this.date_transacted = date_transacted;
        this.groupId = groupId;
        this.amount_transacted=amount_transacted;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getUserid() {
        return userid;
    }

    public String getDate_transacted() {
        return date_transacted;
    }

    public String getGroupId() {
        return groupId;
    }
    public String getamount_transacted() {
        return amount_transacted;
    }
}
