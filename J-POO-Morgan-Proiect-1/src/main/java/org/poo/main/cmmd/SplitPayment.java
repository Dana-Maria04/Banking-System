package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.SplitPaymentTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

public class SplitPayment extends Command {

    public SplitPayment(ExchangeGraph graph, ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, graph, transactions, null);
    }

    @Override
    public void execute() {
        List<String> accounts = getCommand().getAccounts();
        int rate = accounts.size();
        double amountForEach = getCommand().getAmount() / rate;
        String totalDescription = String.format("Split payment of %.2f %s", getCommand().getAmount(), getCommand().getCurrency());

        Account errorAccount = new Account();
        User errorUser = new User();
        boolean insufficientFunds = false;
        for (String iban : accounts) {
            for (User user : getUsers()) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        double convertedAmount = getGraph().convertCurrency(amountForEach, getCommand().getCurrency(), account.getCurrency());
                        if (account.getMinimumBalance() > account.getBalance() - convertedAmount) {
                            errorAccount = account;
                            insufficientFunds = true;
                            errorUser = user;
                        }
                    }
                }
            }
        }

        if (insufficientFunds) {

            for (String iban : accounts) {
                for (User user : getUsers()) {
                    for (Account account : user.getAccounts()) {
                        if (account.getIban().equals(iban)) {
                            SplitPaymentTransaction transaction = new SplitPaymentTransaction(
                                    totalDescription,
                                    getCommand().getTimestamp(),
                                    user.getUser().getEmail(),
                                    amountForEach,
                                    getCommand().getCurrency(),
                                    accounts,
                                    account.getIban(),
                                    "Account " + errorAccount.getIban() + " has insufficient funds for a split payment."
                            );
                            getTransactions().add(transaction);

                        }
                    }
                }
            }
            return ;
        }


        for (String iban : accounts) {
            for (User user : getUsers()) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {

                        SplitPaymentTransaction transaction = new SplitPaymentTransaction(
                                totalDescription,
                                getCommand().getTimestamp(),
                                user.getUser().getEmail(),
                                amountForEach,
                                getCommand().getCurrency(),
                                accounts,
                                account.getIban(),
                                null
                        );


                        double convertedAmount = getGraph().convertCurrency(amountForEach, getCommand().getCurrency(), account.getCurrency());

                        account.setBalance(account.getBalance() - convertedAmount);
                        getTransactions().add(transaction);
                    }
                }
            }
        }


    }

    @Override
    public void undo() {

    }
}