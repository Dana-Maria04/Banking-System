package org.poo.main.userinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.transactions.*;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

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
    private double interestRate;

    public Account(String iban, String accountType, String currency, double balance, ArrayList<Card> cards) {
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.cards = cards;
    }

    public void pay(double amount, String cardNumber, ArrayList<Card> cards, User user,
                    CommandInput command,
                    ArrayList<Transaction> transactions, String iban,
                    ArrayList<PayOnlineTransaction> payOnlineTransactions, Account account) {
        this.foundCard = 0;
        this.insufficientFunds = 0;

        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {

                if (card.getFrozen() == 1) {
                    Transaction transaction = CreateTransaction.getInstance().createTransaction(
                            "FrozePayOnline",
                            Map.of(
                                    "description", "The card is frozen",
                                    "timestamp", command.getTimestamp(),
                                    "email", user.getUser().getEmail(),
                                    "iban", iban
                            )
                    );
                    transactions.add(transaction);
                    this.foundCard = 1;
                    return;
                }

                if (minimumBalance > balance - amount) {
                    Transaction transaction = CreateTransaction.getInstance().createTransaction(
                            "PayOnline",
                            Map.of(
                                    "description", "Insufficient funds",
                                    "timestamp", command.getTimestamp(),
                                    "email", user.getUser().getEmail(),
                                    "amount", amount,
                                    "commerciant", command.getCommerciant(),
                                    "iban", account.getIban()
                            )
                    );
                    transactions.add(transaction);
                    this.insufficientFunds = 1;
                    return;
                }

                PayOnlineTransaction transaction = (PayOnlineTransaction) CreateTransaction.getInstance().createTransaction(
                        "PayOnline",
                        Map.of(
                                "description", "Card payment",
                                "timestamp", command.getTimestamp(),
                                "email", user.getUser().getEmail(),
                                "amount", amount,
                                "commerciant", command.getCommerciant(),
                                "iban", iban
                        )
                );
                transactions.add(transaction);
                payOnlineTransactions.add(transaction);
                this.foundCard = 1;
                this.setBalance(this.balance - amount);

                if (card.getOneTime() == 1) {
                    account.getCards().remove(card);

                    CardDeletionTransaction deleteCardTransaction = new CardDeletionTransaction(
                            "The card has been destroyed",
                            command.getTimestamp(),
                            user.getUser().getEmail(),
                            account.getIban(),
                            card.getCardNumber()
                    );
                    transactions.add(deleteCardTransaction);

                    Card newCard = new Card(Utils.generateCardNumber(), "active", 1);
                    newCard.setFrozen(0);

                    CreateCardTransaction createCardTransaction = new CreateCardTransaction(
                            "New card created",
                            command.getTimestamp(),
                            user.getUser().getEmail(),
                            account.getIban(),
                            newCard.getCardNumber(),
                            user.getUser().getEmail(),
                            account.getIban()
                    );
                    account.getCards().add(newCard);
                    transactions.add(createCardTransaction);
                }
                return;
            }
        }
    }

    public void addResponseToOutput(ObjectMapper objectMapper, ObjectNode commandNode, ArrayNode output, CommandInput command, String description) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        outputNode.put("description", description);
        outputNode.put("timestamp", command.getTimestamp());
        commandNode.put("timestamp", command.getTimestamp());
        commandNode.set("output", outputNode);
        output.add(commandNode);
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