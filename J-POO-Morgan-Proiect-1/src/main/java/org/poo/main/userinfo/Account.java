package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

@Setter
@Getter

public class Account {
    private String iban;
    private double balance;
    private String currency;
    private String accountType;
    private ArrayList<Card> cards;

    // pentru add account
    public Account(String iban, String accountType, String currency, double balance) {
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
    }

    public Account(String iban, String accountType, String currency, double balance, ArrayList<Card> cards) {
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.cards = cards;
    }

}
