package org.poo.main.userinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.transactions.CardDeletionTransaction;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.utils.Utils;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.main.userinfo.transactions.CreateCardTransaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class representing an account of an user.
 */
@Setter
@Getter
@NoArgsConstructor
public class Account {
    private String accountIban;
    private double balance;
    private double minimumBalance;
    private String currency;
    private String accountType;
    private ArrayList<Card> accountCards;
    private int foundCard;
    private int insufficientFunds;
    private double interestRate;

    /**
     * Constructs an Account with the specified details.
     *
     * @param iban        The IBAN of the account.
     * @param accountType The type of the account.
     * @param currency    The currency of the account.
     * @param balance     The balance of the account.
     * @param cards       The list of cards associated with the account.
     */
    public Account(final String iban, final String accountType, final String currency,
                   final double balance, final ArrayList<Card> cards) {
        this.accountIban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.accountCards = cards;
    }

    /**
     * Executes a payment from this account.
     *
     * @param amount               The amount to be paid.
     * @param cardNumber           The card number to be used for the payment.
     * @param cards                The list of cards associated with the account.
     * @param user                 The user associated with the account.
     * @param command              The command containing payment details.
     * @param transactions         The list of transactions to which the transaction will be added.
     * @param iban                 The IBAN associated with the account.
     * @param payOnlineTransactions The list of pay online transactions.
     * @param account              The account from which the payment will be made.
     */
    public void pay(final double amount, final String cardNumber, final ArrayList<Card> cards,
                    final User user, final CommandInput command,
                    final ArrayList<Transaction> transactions, final String iban,
                    final ArrayList<PayOnlineTransaction> payOnlineTransactions,
                    final Account account, final Commerciant commerciant,
                    final ExchangeGraph graph) {
        this.foundCard = 0;
        this.insufficientFunds = 0;

        if(amount == 0) {
            this.insufficientFunds = 1;
            return;
        }

        //amount este amount ul in currency ul contului



        double cashback = 0;
        double ronAmount = graph.convertCurrency(amount, account.getCurrency(), "RON");

        if(commerciant.getCommerciant().getCashbackStrategy() == null) {
            cashback = 0;
            return;
        }


        if(commerciant.getCommerciant().getCashbackStrategy().equals("spendingThreshold")) {
            if((user.getUserPlan().equals("standard") || user.getUserPlan().equals("student"))
                    && command.getCurrency().equals("RON")) {
                // && getCurrency().equals("RON")
                if(ronAmount >= 100 && ronAmount < 300) {
                    cashback = amount * 0.001;
                } else if(ronAmount >= 300 && ronAmount < 500) {
                    cashback = amount * 0.002;
                } else if(ronAmount >= 500) {
                    cashback = amount * 0.0025;
                }
            }
        }



        for (final Card card : cards) {

            System.out.printf("(tmstmp %d) card get number %s si card number %s\n", command.getTimestamp(),card.getCardNumber(), cardNumber);

            if (card.getCardNumber().equals(cardNumber)) {

                if (card.getFrozen() == 1) {
                    final Transaction transaction = CreateTransaction.getInstance()
                            .createTransaction(
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
                // Check if the balance is insufficient to make the payment
                if (minimumBalance > balance - amount) {
                    final Transaction transaction = CreateTransaction.getInstance()
                            .createTransaction(
                            "PayOnline",
                            Map.of(
                                    "description", "Insufficient funds",
                                    "timestamp", command.getTimestamp(),
                                    "email", user.getUser().getEmail(),
                                    "amount", amount,
                                    "commerciant", command.getCommerciant(),
                                    "iban", account.getAccountIban()
                            )
                    );
                    transactions.add(transaction);
                    this.insufficientFunds = 1;
                    return;
                }
                // Proceed with payment as the card is valid and there are sufficient funds
                final PayOnlineTransaction transaction = (PayOnlineTransaction)
                        CreateTransaction.getInstance().createTransaction(
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

                if(user.getUserPlan().equals("standard")) {
                    double comision = amount * 0.002;
                    account.setBalance(account.getBalance() - comision);
                }

                if(user.getUserPlan().equals("silver") && ronAmount >= 500) {
                    double comision = amount * 0.001;
                    account.setBalance(account.getBalance() - comision);
                }



                this.foundCard = 1;
                this.setBalance(this.balance - amount + cashback);

                if (card.getOneTime() == 1) {
                    account.getAccountCards().remove(card);

                    final CardDeletionTransaction deleteCardTransaction =
                            new CardDeletionTransaction(
                            "The card has been destroyed",
                            command.getTimestamp(),
                            user.getUser().getEmail(),
                            account.getAccountIban(),
                            card.getCardNumber()
                    );
                    transactions.add(deleteCardTransaction);

                    final Card newCard = new Card(Utils.generateCardNumber(),
                            "active", 1);
                    newCard.setFrozen(0);

                    final CreateCardTransaction createCardTransaction =
                            new CreateCardTransaction(
                            "New card created",
                            command.getTimestamp(),
                            user.getUser().getEmail(),
                            account.getAccountIban(),
                            newCard.getCardNumber(),
                            user.getUser().getEmail(),
                            account.getAccountIban()
                    );
                    account.getAccountCards().add(newCard);
                    transactions.add(createCardTransaction);
                }
                return;
            }
        }
    }

    /**
     * Adds a response to the output.
     *
     * @param objectMapper  The ObjectMapper used to create nodes.
     * @param commandNode   The command node to which the output will be added.
     * @param output        The output array node.
     * @param command       The command containing the input parameters.
     * @param description   The description to be added to the response.
     */
    public void addResponseToOutput(final ObjectMapper objectMapper, final ObjectNode commandNode,
                                    final ArrayNode output,
                                    final CommandInput command, final String description) {
        final ObjectNode outputNode = objectMapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        outputNode.put("description", description);
        outputNode.put("timestamp", command.getTimestamp());
        commandNode.put("timestamp", command.getTimestamp());
        commandNode.set("output", outputNode);
        output.add(commandNode);
    }

    /**
     * Searches for an account by its IBAN.
     *
     * @param iban      The IBAN to search for.
     * @param accounts  The list of accounts to search in.
     * @return The account matching the IBAN, or null if not found.
     */
    public static Account searchAccount(final String iban, final ArrayList<Account> accounts) {
        for (final Account account : accounts) {
            if (account.getAccountIban().equals(iban)) {
                return account;
            }
        }
        return null;
    }

    public int verifyIfClassicAccount(final ArrayList<Account> accounts) {
        for (Account account : accounts) {
            if (account.getAccountType().equals("classic")) {
                return 1;
            }
        }
        return 0;
    }


    /**
     * Increases the balance of the account by the given amount.
     *
     * @param amount The amount to increase the balance by.
     */
    public void incBalance(final double amount) {
        this.balance += amount;
    }

    /**
     * Decreases the balance of the account by the given amount.
     *
     * @param amount The amount to decrease the balance by.
     */
    public void decBalance(final double amount) {
        this.balance -= amount;
    }
}
