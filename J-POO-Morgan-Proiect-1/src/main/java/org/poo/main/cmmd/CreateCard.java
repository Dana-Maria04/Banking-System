package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.transactions.CreateCardTransaction;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class CreateCard extends Command {
    private int oneTime;

    public CreateCard(ArrayList<User> users, CommandInput command, int oneTime, ArrayList<Transaction> transactions) {
        super(users, null, null, command, null, null, transactions);
        this.oneTime = oneTime;
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(getCommand().getAccount())) {

                        Card newCard = new Card(Utils.generateCardNumber(), "active", oneTime);
                        newCard.setFrozen(0);

                        CreateCardTransaction transaction = new CreateCardTransaction(
                                "New card created",
                                getCommand().getTimestamp(),
                                user.getUser().getEmail(),
                                account.getIban(),
                                newCard.getCardNumber(),
                                user.getUser().getEmail()
                        );

                        getTransactions().add(transaction);

                        account.getCards().add(newCard);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}
