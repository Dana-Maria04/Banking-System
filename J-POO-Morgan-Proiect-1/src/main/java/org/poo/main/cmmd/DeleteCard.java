package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.transactions.CardDeletionTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class DeleteCard extends Command {

    public DeleteCard(ArrayList<User> users, CommandInput command, ArrayList<Transaction> transactions) {
        super(users, null, null, command, null, null, transactions, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (Account account : user.getAccounts()) {
                    Card cardToDelete = new Card();
                    for (Card c : account.getCards()) {
                        if (c.getCardNumber().equals(getCommand().getCardNumber())) {
                            cardToDelete = c;
                            break;
                        }
                    }

                    if (cardToDelete != null) {
                        account.getCards().remove(cardToDelete);

                        CardDeletionTransaction transaction = new CardDeletionTransaction(
                                "The card has been destroyed",
                                getCommand().getTimestamp(),
                                user.getUser().getEmail(),
                                account.getIban(),
                                cardToDelete.getCardNumber()
                        );

                        getTransactions().add(transaction);
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