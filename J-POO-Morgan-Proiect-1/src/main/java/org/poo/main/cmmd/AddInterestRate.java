package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

/**
 * Represents a command to add interest to an account's balance.
 * This command checks if the account is of the "savings" type and applies the interest
 * rate to the balance. If the account is not a savings account, it adds a response to
 * the output.
 */
public class AddInterestRate extends Command {

    /**
     * Constructor to initialize the AddInterestRate command with necessary parameters.
     *
     * @param users         List of users to be processed
     * @param commandNode   JSON node representing the command input
     * @param output        ArrayNode to store output results
     * @param command       Command input data
     * @param objectMapper  ObjectMapper for JSON operations
     */
    public AddInterestRate(final ArrayList<User> users, final ObjectNode commandNode,
                           final ArrayNode output, final CommandInput command,
                           final ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper, null, null, null);
    }

    /**
     * Executes the AddInterestRate command.
     * This method checks whether the account is of type "savings" and applies the interest rate
     * to the balance. If the account is not of type "savings", it adds an appropriate response to
     * the output.
     *
     * @throws IllegalArgumentException if the account with the given IBAN is not found
     */
    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(getCommand().getAccount())) {

                    if (account.getAccountType().equals("classic")) {
                        account.addResponseToOutput(getObjectMapper(), getCommandNode(),
                                getOutput(), getCommand(), "This is not a savings account");
                        return;
                    }

                    account.setBalance(account.getBalance()
                            + account.getBalance() * account.getInterestRate());

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
