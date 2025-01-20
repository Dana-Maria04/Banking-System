package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Map;

public class UpgradePlan extends Command{


    public UpgradePlan(final ExchangeGraph graph, final CommandInput commandInput,
                           final ArrayList<Transaction> transactions, final ArrayList<User> users) {
        super(users, null, null, commandInput, null,
                graph, transactions, null, null);
    }

    @Override
    public void execute() {
        for(User user : getUsers()) {
            for(Account account : user.getAccounts()) {
                if(account.getAccountIban().equals(getCommand().getAccount())) {
                    if((user.getUserPlan().equals("standard") || user.getUserPlan().equals("student"))
                       && getCommand().getNewPlanType().equals("silver")) {
                        double rightAmount = getGraph().convertCurrency(100,
                                "RON", account.getCurrency());
                        if(account.getBalance() - account.getMinimumBalance() >= rightAmount) {
                            account.setBalance(account.getBalance() - rightAmount);

                            // aici vine fast o tranzactie


                            Map<String, Object> params = Map.of(
                                    "description", "Upgrade plan",
                                    "timestamp", getCommand().getTimestamp(),
                                    "email", user.getUser().getEmail(),
                                    "newPlanType", getCommand().getNewPlanType(),
                                    "iban", account.getAccountIban()
                            );
                            Transaction transaction = CreateTransaction.getInstance()
                                    .createTransaction("UpgradePlan", params);
                            getTransactions().add(transaction);


                            user.setUserPlan("silver");
                        }
                    } else if(user.getUserPlan().equals("silver")
                            && getCommand().getNewPlanType().equals("gold")) {

                        double rightAmount = getGraph().convertCurrency(250,
                                "RON", account.getCurrency());

                        // si platile de peste 500

                        if(account.getBalance() - account.getMinimumBalance() >= rightAmount) {
                            account.setBalance(account.getBalance() - rightAmount);

                            // aici vine fast o tranzactie
                            Map<String, Object> params = Map.of(
                                    "description", "Upgrade plan",
                                    "timestamp", getCommand().getTimestamp(),
                                    "email", user.getUser().getEmail(),
                                    "newPlanType", getCommand().getNewPlanType(),
                                    "iban", account.getAccountIban()
                            );
                            Transaction transaction = CreateTransaction.getInstance()
                                    .createTransaction("UpgradePlan", params);
                            getTransactions().add(transaction);


                            user.setUserPlan("gold");
                        }
                    } else if((user.getUserPlan().equals("standard") || user.getUserPlan().equals("student"))
                            && getCommand().getNewPlanType().equals("gold")) {
                        double rightAmount = getGraph().convertCurrency(350,
                                "RON", account.getCurrency());

                        if(account.getBalance() - account.getMinimumBalance() >= rightAmount) {
                            account.setBalance(account.getBalance() - rightAmount);

                            // aici vine fast o tranzactie

                            Map<String, Object> params = Map.of(
                                    "description", "Upgrade plan",
                                    "timestamp", getCommand().getTimestamp(),
                                    "email", user.getUser().getEmail(),
                                    "newPlanType", getCommand().getNewPlanType(),
                                    "iban", account.getAccountIban()
                            );
                            Transaction transaction = CreateTransaction.getInstance()
                                    .createTransaction("UpgradePlan", params);
                            getTransactions().add(transaction);

                            user.setUserPlan("gold");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}
