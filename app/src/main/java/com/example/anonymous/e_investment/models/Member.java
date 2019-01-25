package com.example.anonymous.e_investment.models;

public class Member {
    String member_id,username,phone,email,password,picurl;

    public Member() {
    }

    public Member(String member_id, String username, String phone, String email, String password, String picurl) {
        this.member_id = member_id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.picurl = picurl;
    }

    public String getMember_id() {
        return member_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPicurl() {
        return picurl;
    }

}
