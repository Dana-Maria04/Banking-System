package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class Command {
    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;
    private ExchangeGraph graph;
    private ArrayList<Transaction> transactions;
    private ArrayList<PayOnlineTransaction> spendingsReportTransactions;

    public Command(ArrayList<User> users, ObjectNode commandNode, ArrayNode output,
                   CommandInput command, ObjectMapper objectMapper, ExchangeGraph graph,
                   ArrayList<Transaction> transactions,
                   ArrayList<PayOnlineTransaction> spendingsReportTransactions) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
        this.graph = graph;
        this.transactions = transactions;
        this.spendingsReportTransactions = spendingsReportTransactions;
    }

    protected void addResponseToOutput(String key, String message) {
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put(key, message);
        responseNode.put("timestamp", command.getTimestamp());

        commandNode.put("command", command.getCommand());
        commandNode.set("output", responseNode);
        commandNode.put("timestamp", command.getTimestamp());
        output.add(commandNode);

    }

    protected Map<String, Object> constructParams(String description, Map<String, Object> additionalParams) {
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

    public abstract void execute();

    public abstract void undo();
}