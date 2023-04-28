package org.example.model;

import java.time.LocalDate;

public class Subscription {

    private BankCard bankcard;
    private LocalDate startDate;

    public Subscription(BankCard bankCard, LocalDate date) {
        this.bankcard = bankCard;
        this.startDate = date;
    }

    public BankCard getBankcard() {
        return bankcard;
    }

    public void setBankcard(BankCard bankcard) {
        this.bankcard = bankcard;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}