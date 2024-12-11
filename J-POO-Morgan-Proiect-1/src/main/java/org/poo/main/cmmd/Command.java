package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

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


    public Command(ArrayList<User> users, ObjectNode commandNode, ArrayNode output,
                   CommandInput command, ObjectMapper objectMapper, ExchangeGraph graph,
                   ArrayList<Transaction> transactions) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
        this.graph = graph;
        this.transactions = transactions;
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

    public abstract void execute();

    public abstract void undo();
}
