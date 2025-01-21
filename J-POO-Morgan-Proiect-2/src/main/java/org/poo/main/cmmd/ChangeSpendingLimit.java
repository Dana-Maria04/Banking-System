package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.BusinessAccount;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

/**
 * The ChangeSpendingLimit class is responsible for handling the command
 * to change the spending limit of a business account. Only the owner of the
 * account can change the spending limit.
 */
public class ChangeSpendingLimit extends Command {

    /**
     * Constructs a ChangeSpendingLimit command.
     *
     * @param users            The list of users in the system.
     * @param command          The command input containing details about the
     *                         spending limit change.
     * @param transactions     The list of transactions for logging purposes.
     * @param businessAccounts The list of business accounts in the system.
     * @param objectMapper     The ObjectMapper instance for JSON operations.
     * @param output           The output array node where the results of the command
     *                         will be stored.
     */
    public ChangeSpendingLimit(final ArrayList<User> users, final CommandInput command,
                               final ArrayList<Transaction> transactions,
                               final ArrayList<BusinessAccount> businessAccounts,
                               final ObjectMapper objectMapper, final ArrayNode output) {
        super(users, null, output,
                command, objectMapper, null, transactions,
                null, null, null, businessAccounts);
    }

    /**
     * Executes the ChangeSpendingLimit command. It checks if the user is the owner of
     * the business account and updates the spending limit if the user has the required privileges.
     * If the user is not the owner, an appropriate error message is added to the output.
     */
    @Override
    public void execute() {
        boolean isOwner = false;

        for (BusinessAccount account : getBusinessAccounts()) {
            if (account.getOwnerEmail().equals(getCommand().getEmail())) {
                account.setSpendingLimit(getCommand().getAmount());
                isOwner = true;
                break;
            }
        }

        if (!isOwner) {
            ObjectNode outputNode = getObjectMapper().createObjectNode();
            outputNode.put("description", "You must be owner in order"
                    + " to change spending limit.");
            outputNode.put("timestamp", getCommand().getTimestamp());

            ObjectNode commandNode = getObjectMapper().createObjectNode();
            commandNode.put("command", getCommand().getCommand());
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", getCommand().getTimestamp());

            getOutput().add(commandNode);
        }
    }

    /**
     * Provides functionality for undoing the command. Currently, this method is
     * reserved for future development.
     */
    @Override
    public void undo() {
    }
}
