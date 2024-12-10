package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.Transaction;
import org.poo.main.userinfo.User;
import org.poo.utils.Utils;
import org.poo.main.cmmd.Command;
import java.util.ArrayList;

public class CreateCard extends Command {
    private int oneTime;

    public CreateCard(ArrayList<User> users, CommandInput command, int oneTime) {
        super(users, null, null, command, null, null);
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
                        Transaction transaction = new Transaction();
                        transaction.setAccount(account.getIban());
                        transaction.setCardHolder(user.getUser().getEmail());
                        transaction.setCardNumber(newCard.getCardNumber());
                        transaction.setDescription("New card created");
                        transaction.setTimestamp(getCommand().getTimestamp());

                        user.getTransactions().add(transaction);

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
