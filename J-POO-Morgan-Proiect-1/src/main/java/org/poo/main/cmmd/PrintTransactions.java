package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class PrintTransactions extends Command {
    public PrintTransactions(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                             ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, null);
    }

    @Override
    public void execute() {
        getCommandNode().put("command", getCommand().getCommand());
        getCommandNode().put("timestamp", getCommand().getTimestamp());

        ArrayNode transactionsArray = getObjectMapper().createArrayNode();

        for (Transaction transaction : getTransactions()) {
            if (transaction.getEmail() == null || !transaction.getEmail().equals(getCommand().getEmail())) {
                continue;
            }

            ObjectNode transactionNode = getObjectMapper().createObjectNode();
            transactionNode.put("timestamp", transaction.getTimestamp());
            transactionNode.put("description", transaction.getDescription());

            transaction.addDetailsToNode(transactionNode);

            transactionsArray.add(transactionNode);
        }

        getCommandNode().set("output", transactionsArray);
        getOutput().add(getCommandNode());
    }

    @Override
    public void undo() {

    }
}