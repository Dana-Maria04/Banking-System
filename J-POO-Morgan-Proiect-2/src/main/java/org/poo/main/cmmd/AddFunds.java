package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

/**
 * Represents a command to add funds to an existing account.
 * This command processes the addition of a specified amount of funds to a user's account.
 */
public class AddFunds extends Command {

    /**
     * Constructor to initialize the AddFunds command with necessary parameters.
     *
     * @param users         List of users to be processed
     * @param commandNode   JSON node representing the command input
     * @param output        ArrayNode to store output results
     * @param command       Command input data
     * @param objectMapper  ObjectMapper for JSON operations
     */
    public AddFunds(final ArrayList<User> users, final ObjectNode commandNode,
                    final ArrayNode output, final CommandInput command,
                    final ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper, null,
                null, null, null, null);
    }

    /**
     * Executes the AddFunds command.
     * This method adds the specified amount of funds to the account identified by the IBAN in
     * the command input. If the account is found, its balance is updated, otherwise, an exception
     * is thrown.
     */
    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(getCommand().getAccount())) {
                    account.setBalance(account.getBalance() + getCommand().getAmount());
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
