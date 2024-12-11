package org.poo.main.userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.transactions.FrozenPayOnlineTransaction;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
public class Account {
    private String iban;
    private double balance;
    private double minimumBalance;
    private String currency;
    private String accountType;
    private ArrayList<Card> cards;
    private int foundCard;
    private int insufficientFunds;

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

    public void pay(double amount, String cardNumber, ArrayList<Card> cards, User user, CommandInput command,
                    ArrayList<Transaction> transactions) {
        this.foundCard = 0;
        this.insufficientFunds = 0;

        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {

                if (card.getFrozen() == 1) {
                    FrozenPayOnlineTransaction transaction = new FrozenPayOnlineTransaction(
                            "The card is frozen",
                            command.getTimestamp(),
                            user.getUser().getEmail()
                    );

                    transactions.add(transaction);
                    this.foundCard = 1;
                    return;
                }

                if (minimumBalance > balance - amount) {
                    PayOnlineTransaction transaction = new PayOnlineTransaction(
                            "Insufficient funds",
                            command.getTimestamp(),
                            user.getUser().getEmail(),
                            amount,
                            command.getCommerciant()
                    );

                    transactions.add(transaction);
                    this.insufficientFunds = 1;
                    return;
                }

                PayOnlineTransaction transaction = new PayOnlineTransaction(
                        "Card payment",
                        command.getTimestamp(),
                        user.getUser().getEmail(),
                        amount,
                        command.getCommerciant()
                );

                transactions.add(transaction);
                this.foundCard = 1;

                this.setBalance(this.balance - amount);
                return;
            }
        }
    }



    public static Account searchAccount(String iban, ArrayList<Account> accounts) {
        for(Account account : accounts) {
            if(account.getIban().equals(iban)) {
                return account;
            }
        }
        return null;

    }

    public void incBalance (double amount) {
            this.balance += amount;
    }

    public void decBalance (double amount) {
            this.balance -= amount;
    }

}
