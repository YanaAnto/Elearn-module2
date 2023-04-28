package org.example.model;


public class BankCard {

    private String number;
    private User user;

    public BankCard(User user) {
        this.user = user;
    }

    public BankCard() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
