package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.BusinessAccount;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * The CreateCard class creates new cards for a given account.
 * It is used to generate a new card for the user and adds it to the specified account.
 */
public class CreateCard extends Command {
    private final int oneTime;

    /**
     * Constructs a new CreateCard command with the specified parameters.
     *
     * @param users       The list of users
     * @param command     The command input
     * @param oneTime     Indicates if the card is a one-time card (1 for one-time, 0 for regular)
     * @param transactions The list of transactions to which the new transaction will be added
     */
    public CreateCard(final ArrayList<User> users, final CommandInput command, final int oneTime,
                      final ArrayList<Transaction> transactions,
                      final ArrayList<BusinessAccount> businessAccounts) {
        super(users, null, null,
                command, null, null, transactions,
                null, null, null, businessAccounts);
        this.oneTime = oneTime;
    }

    /**
     * Executes the CreateCard command, generating a new card and adding it to the
     * specified account. It also logs the transaction associated with the card creation.
     */
    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {

                for (Account account : user.getAccounts()) {
                    if (account.getAccountIban().equals(getCommand().getAccount())) {

                        final Card newCard = new Card(Utils.generateCardNumber(),
                                "active", oneTime);
                        newCard.setFrozen(0);

                        final Map<String, Object> params = Map.of(
                                "description", "New card created",
                                "timestamp", getCommand().getTimestamp(),
                                "email", user.getUser().getEmail(),
                                "iban", account.getAccountIban(),
                                "cardNumber", newCard.getCardNumber(),
                                "userEmail", user.getUser().getEmail()
                        );
                        final Transaction transaction = CreateTransaction.getInstance()
                                .createTransaction("CreateCard", params);
                        getTransactions().add(transaction);

                        account.getAccountCards().add(newCard);
                        break;
                    }
                }
                break;
            }
        }


        if (getBusinessAccounts() == null) {
            return;
        }

        for (BusinessAccount account : getBusinessAccounts()) {
            if (account.getAccountIban().equals(getCommand().getAccount())) {

                final Card newCard = new Card(Utils.generateCardNumber(),
                        "active", oneTime);
                newCard.setFrozen(0);

                final Map<String, Object> params = Map.of(
                        "description", "New card created",
                        "timestamp", getCommand().getTimestamp(),
                        "email", getCommand().getEmail(),
                        "iban", account.getAccountIban(),
                        "cardNumber", newCard.getCardNumber(),
                        "userEmail", getCommand().getEmail()
                );

                final Transaction transaction = CreateTransaction.getInstance()
                        .createTransaction("CreateCard", params);
                getTransactions().add(transaction);
                account.getCards().add(account.getCards().size(), newCard);
                break;
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
