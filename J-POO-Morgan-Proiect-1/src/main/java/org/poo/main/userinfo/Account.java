package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

@Setter
@Getter

public class Account {
    private String iban;
    private double balance;
    private String currency;
    private String accountType;
    private Card[] cards;

    // pentru add account
    public Account(String iban, String accountType, String currency) {
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
    }

    public Account(String iban, String accountType, String currency, double balance, Card[] cards) {
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.cards = cards;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
