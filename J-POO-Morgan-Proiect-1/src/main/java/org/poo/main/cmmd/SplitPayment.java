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
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        boolean insufficientFunds = false;
        for (String iban : accounts) {
            for (User user : getUsers()) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        double convertedAmount = getGraph().convertCurrency(amountForEach, getCommand().getCurrency(), account.getCurrency());
                        if (account.getMinimumBalance() > account.getBalance() - convertedAmount) {
                            errorAccount = account;
                            insufficientFunds = true;
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
                            Map<String, Object> params = new HashMap<>();
                            params.put("description", totalDescription);
                            params.put("timestamp", getCommand().getTimestamp());
                            params.put("email", user.getUser().getEmail());
                            params.put("amount", amountForEach);
                            params.put("currency", getCommand().getCurrency());
                            params.put("involvedAccounts", accounts);
                            params.put("iban", account.getIban());
                            params.put("error", "Account " + errorAccount.getIban() + " has insufficient funds for a split payment.");

                            SplitPaymentTransaction transaction = (SplitPaymentTransaction)
                                    CreateTransaction.getInstance().createTransaction("SplitPayment", params);
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

                        Map<String, Object> params = new HashMap<>();
                        params.put("description", totalDescription);
                        params.put("timestamp", getCommand().getTimestamp());
                        params.put("email", user.getUser().getEmail());
                        params.put("amount", amountForEach);
                        params.put("currency", getCommand().getCurrency());
                        params.put("involvedAccounts", accounts);
                        params.put("iban", account.getIban());
                        params.put("error", null);

                        SplitPaymentTransaction transaction = (SplitPaymentTransaction)
                                CreateTransaction.getInstance().createTransaction("SplitPayment", params);


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