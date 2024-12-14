package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.ReportTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

public class Report extends Command {

    public Report(ArrayList<User> users, CommandInput command, ExchangeGraph exchangeGraph,
                  ArrayNode output, ObjectMapper objectMapper, ObjectNode commandNode,
                  ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, exchangeGraph, transactions, null);
    }

    @Override
    public void execute() {
        String targetIban = getCommand().getAccount();
        int startTimestamp = getCommand().getStartTimestamp();
        int endTimestamp = getCommand().getEndTimestamp();

        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(targetIban)) {
                    ReportTransaction reportTransaction = new ReportTransaction(
                            getCommand().getDescription(),
                            getCommand().getTimestamp(),
                            user.getUser().getEmail(),
                            targetIban,
                            startTimestamp,
                            endTimestamp,
                            account,
                            new ArrayList<>(getTransactions()),
                            user,
                            account.getIban()
                    );

                    ObjectNode transactionNode = getObjectMapper().createObjectNode();
                    reportTransaction.addDetailsToNode(transactionNode);
                    getOutput().add(transactionNode);
                    return;
                }
            }
        }

        ObjectNode errorNode = getObjectMapper().createObjectNode();
        errorNode.put("command", "report");
        ObjectNode outputDetails = errorNode.putObject("output");
        outputDetails.put("description", "Account not found");
        outputDetails.put("timestamp", getCommand().getTimestamp());
        errorNode.put("timestamp", getCommand().getTimestamp());
        getOutput().add(errorNode);
    }

    @Override
    public void undo() {

    }
}