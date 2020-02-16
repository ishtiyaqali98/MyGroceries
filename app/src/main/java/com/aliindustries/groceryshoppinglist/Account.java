package com.aliindustries.groceryshoppinglist;

public class Account {

    String id;
    String email;
    String hashpassword;


    public Account(String id, String email, String hashpassword) {
        this.id = id;
        this.email = email;
        this.hashpassword = hashpassword;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getHashpassword() {
        return hashpassword;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashpassword(String hashpassword) {
        this.hashpassword = hashpassword;
    }
}
