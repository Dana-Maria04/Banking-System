package org.poo.main.userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poo.fileio.CommandInput;

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

    public void pay(double amount, String cardNumber, ArrayList<Card> Cards, User user, CommandInput command) {
        this.foundCard = 0;
        this.insufficientFunds = 0;
        for(Card card : Cards) {
            if(card.getCardNumber().equals(cardNumber)) {

                if(card.getFrozen() == 1) {
                    Transaction transaction = new Transaction();
                    transaction.setTimestamp(command.getTimestamp());
                    transaction.setDescription("The card is frozen");
                    this.foundCard = 1;
                    user.getTransactions().add(transaction);

                    return;
                }


                if(minimumBalance > balance - amount) {

                    Transaction transaction = new Transaction();
                    transaction.setTimestamp(command.getTimestamp());
                    transaction.setDescription("Insufficient funds");
                    user.getTransactions().add(transaction);

                    this.insufficientFunds = 1;
                    return;
                }

                Transaction transaction = new Transaction();
                transaction.setTimestamp(command.getTimestamp());
                transaction.setAmountPayOnline(amount);
                transaction.setDescription("Card payment");
                transaction.setCommerciant(command.getCommerciant());

                user.getTransactions().add(transaction);

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
