package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

/**
 * The SetMinimumBalance class represents a command to set the minimum balance for a specified
 * account. It validates the account and sets the minimum balance if the account is found.
 */
public class SetMinimumBalance extends Command {

    /**
     * Constructs a SetMinimumBalance command with the provided parameters.
     *
     * @param users        The list of users.
     * @param command     The command input data.
     */
    public SetMinimumBalance(final ArrayList<User> users, final CommandInput command) {
        super(users, null, null, command,
                null, null, null,
                null, null);
    }

    /**
     * Executes the SetMinimumBalance command. It finds the account by IBAN and sets the minimum
     * balance.
     *
     * @throws IllegalArgumentException if the account is not found.
     */
    @Override
    public void execute() {
        for (final User user : getUsers()) {
            for (final Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(getCommand().getAccount())) {
                    account.setMinimumBalance(getCommand().getMinBalance());
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Account not found");
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
