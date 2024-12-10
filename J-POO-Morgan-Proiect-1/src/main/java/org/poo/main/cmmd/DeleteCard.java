package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.Transaction;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class DeleteCard extends Command {

    public DeleteCard(ArrayList<User> users, CommandInput command) {
        super(users, null, null, command, null, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (Account account : user.getAccounts()) {
                    Card cardToDelete = null;
                    for (Card c : account.getCards()) {
                        if (c.getCardNumber().equals(getCommand().getCardNumber())) {
                            cardToDelete = c;
                            break;
                        }
                    }

                    if (cardToDelete != null) {
                        account.getCards().remove(cardToDelete);

                        Transaction transaction = new Transaction();
                        transaction.setAccount(account.getIban());
                        transaction.setCardHolder(user.getUser().getEmail());
                        transaction.setCardNumber(cardToDelete.getCardNumber());
                        transaction.setDescription("The card has been destroyed");
                        transaction.setTimestamp(getCommand().getTimestamp());

                        user.getTransactions().add(transaction);
                    }
                }
                return;
            }
        }
    }

    @Override
    public void undo() {

    }
}
