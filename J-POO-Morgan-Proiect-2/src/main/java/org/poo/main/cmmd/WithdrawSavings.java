package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

public class WithdrawSavings extends Command{



    public WithdrawSavings(final ExchangeGraph graph, final CommandInput commandInput,
                           final ArrayList<Transaction> transactions, final ArrayList<User> users) {
        super(users, null, null, commandInput, null,
                graph, transactions, null, null);
    }

    @Override
    public void execute() {
        for(User user : getUsers()) {
            for(Account account : user.getAccounts()) {
                if(account.getAccountIban().equals(getCommand().getAccount())
                        && account.getAccountType().equals("savings")
                        && account.verifyIfClassicAccount(user.getAccounts()) == 1) {
                    if(user.checkOldEnough() == 1) {
                        double amount = getCommand().getAmount();
                        double righAmount = getGraph().convertCurrency(amount, getCommand().getCurrency(), account.getCurrency());
                        account.setBalance(account.getBalance() - righAmount);
                        for(Account account1 : user.getAccounts()) {
                            if(account1.getAccountType().equals("classic")) {
                                account1.setBalance(account1.getBalance() + righAmount);
                                break;
                            }
                        }
                    } else {
                        Map<String, Object> params = Map.of(
                                "description", "You don't have the minimum age required.",
                                "timestamp", getCommand().getTimestamp(),
                                "email", user.getUser().getEmail()
                        );
                        Transaction transaction = CreateTransaction.getInstance()
                                .createTransaction("WithdrawSavings", params);
                        getTransactions().add(transaction);
                    }
                    return ;
                } else if (account.getAccountIban().equals(getCommand().getAccount())
                        && account.getAccountType().equals("savings")
                        && account.verifyIfClassicAccount(user.getAccounts()) == 0) {
                    Map<String, Object> params = Map.of(
                            "description", "You do not have a classic account.",
                            "timestamp", getCommand().getTimestamp(),
                            "email", user.getUser().getEmail()
                    );
                    Transaction transaction = CreateTransaction.getInstance()
                            .createTransaction("WithdrawSavings", params);
                    getTransactions().add(transaction);
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}
