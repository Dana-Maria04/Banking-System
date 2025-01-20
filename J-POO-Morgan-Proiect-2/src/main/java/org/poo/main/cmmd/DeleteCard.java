package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.transactions.CardDeletionTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

/**
 * The DeleteCard class handles the deletion of a specific card from a user's account.
 * If the card is found, it is removed, and a corresponding transaction is created to
 * log the action.
 */
public class DeleteCard extends Command {

    /**
     * Constructs a DeleteCard command with the specified parameters.
     *
     * @param users        The list of users
     * @param command      The command input
     * @param transactions The list of transactions to log the action
     */
    public DeleteCard(final ArrayList<User> users, final CommandInput command,
                      final ArrayList<Transaction> transactions) {
        super(users, null, null, command,
                null, null, transactions, null, null, null);
    }

    /**
     * Executes the DeleteCard command by searching for the specified card number.
     * If the card is found, it is removed from the account, and a corresponding transaction
     * is created. If the card is not found, no action is taken.
     */
    @Override
    public void execute() {
        for (final User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (final Account account : user.getAccounts()) {
                    Card cardToDelete = new Card();
                    for (final Card c : account.getAccountCards()) {
                        if (c.getCardNumber().equals(getCommand().getCardNumber())) {
                            cardToDelete = c;
                            break;
                        }
                    }

                    account.getAccountCards().remove(cardToDelete);

                    final CardDeletionTransaction transaction = new CardDeletionTransaction(
                            "The card has been destroyed",
                            getCommand().getTimestamp(),
                            user.getUser().getEmail(),
                            account.getAccountIban(),
                            cardToDelete.getCardNumber()
                    );

                    getTransactions().add(transaction);
                }

                return;
            }
        }
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
