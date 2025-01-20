package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Commerciant;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class representing a command.
 * This class serves as the base class for all commands and provides common functionality for
 * handling input, output, and transactions.
 */
@Getter
@Setter
public abstract class Command {
    private final ArrayList<User> users;
    private final ObjectNode commandNode;
    private final ArrayNode output;
    private final CommandInput command;
    private final ObjectMapper objectMapper;
    private final ExchangeGraph graph;
    private final ArrayList<Transaction> transactions;
    private final ArrayList<PayOnlineTransaction> spendingsReportTransactions;
    private final ArrayList<Commerciant> commerciants;

    /**
     * Constructor for the Command class, initializing the parameters needed for
     * the command to execute.
     *
     * @param users                  List of users involved in the command
     * @param commandNode            The JSON node representing the command input
     * @param output                 The output array node where results are stored
     * @param command                The command input data
     * @param objectMapper           ObjectMapper for JSON operations
     * @param graph                  The currency exchange graph
     * @param transactions           List of transactions to be processed
     * @param spendingsReportTransactions List of transactions related to the spending report
     */
    public Command(final ArrayList<User> users, final ObjectNode commandNode,
                   final ArrayNode output, final CommandInput command,
                   final ObjectMapper objectMapper, final ExchangeGraph graph,
                   final ArrayList<Transaction> transactions,
                   final ArrayList<PayOnlineTransaction> spendingsReportTransactions,
                   final ArrayList<Commerciant> commerciants) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
        this.graph = graph;
        this.transactions = transactions;
        this.spendingsReportTransactions = spendingsReportTransactions;
        this.commerciants = commerciants;
    }

    /**
     * Adds a response message to the output.
     *
     * @param key     The key under which the message is stored
     * @param message The message to be added to the response
     */
    protected void addResponseToOutput(final String key, final String message) {
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put(key, message);
        responseNode.put("timestamp", command.getTimestamp());

        commandNode.put("command", command.getCommand());
        commandNode.set("output", responseNode);
        commandNode.put("timestamp", command.getTimestamp());
        output.add(commandNode);
    }

    /**
     * Constructs the parameters for a transaction, combining the given description
     * and additional parameters.
     *
     * @param description    The description for the transaction
     * @param additionalParams Additional parameters to include in the transaction
     * @return A map containing all the parameters for the transaction
     */
    protected Map<String, Object> constructParams(final String description,
                                                  final Map<String, Object> additionalParams) {
        Map<String, Object> params = new HashMap<>();
        params.put("description", description);
        params.put("timestamp", command.getTimestamp());

        if (command.getEmail() != null) {
            params.put("email", command.getEmail());
        }
        if (command.getAccount() != null) {
            params.put("iban", command.getAccount());
        }

        if (additionalParams != null) {
            params.putAll(additionalParams);
        }

        return params;
    }

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Undoes the command.
     * For future development.
     */
    public abstract void undo();
}
