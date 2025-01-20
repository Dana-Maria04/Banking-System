package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

/**
 * The PrintTransactions class handles the execution of the "printTransactions" command.
 * It retrieves and prints transactions related to a specific user based on the provided email.
 */
public class PrintTransactions extends Command {

    /**
     * Constructs a PrintTransactions command with the specified parameters.
     *
     * @param users           The list of users
     * @param commandNode     The command node containing information about the command
     * @param output          The array node to store the output
     * @param command         The command input
     * @param objectMapper    ObjectMapper for JSON operations
     * @param transactions    The list of transactions to retrieve
     */
    public PrintTransactions(final ArrayList<User> users, final ObjectNode commandNode,
                             final ArrayNode output, final CommandInput command,
                             final ObjectMapper objectMapper,
                             final ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null,
                transactions, null, null);
    }

    /**
     * Executes the printTransactions command by fetching and displaying transactions for the user
     * based on the provided email.
     */
    @Override
    public void execute() {
        getCommandNode().put("command", getCommand().getCommand());
        getCommandNode().put("timestamp", getCommand().getTimestamp());

        final ArrayNode transactionsArray = getObjectMapper().createArrayNode();
        boolean foundTransactions = false;

        for (final Transaction transaction : getTransactions()) {
            if (transaction.getEmail() == null || !transaction.getEmail()
                    .equals(getCommand().getEmail())) {
                continue;
            }

            final ObjectNode transactionNode = getObjectMapper().createObjectNode();
            transactionNode.put("timestamp", transaction.getTimestamp());
            transactionNode.put("description", transaction.getDescription());

            transaction.addDetailsToNode(transactionNode);

            transactionsArray.add(transactionNode);
            foundTransactions = true;
        }

        if (!foundTransactions) {
            throw new IllegalArgumentException("No transactions found for the provided email.");
        }

        getCommandNode().set("output", transactionsArray);
        getOutput().add(getCommandNode());
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
