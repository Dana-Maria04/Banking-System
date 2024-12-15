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
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

                    Map<String, Object> params = new HashMap<>();
                    params.put("description", getCommand().getDescription());
                    params.put("timestamp", getCommand().getTimestamp());
                    params.put("email", user.getUser().getEmail());
                    params.put("targetIban", targetIban);
                    params.put("startTimestamp", startTimestamp);
                    params.put("endTimestamp", endTimestamp);
                    params.put("account", account);
                    params.put("transactions", new ArrayList<>(getTransactions()));
                    params.put("user", user);
                    params.put("reportIban", account.getIban());

                    ReportTransaction reportTransaction = (ReportTransaction) CreateTransaction.getInstance()
                            .createTransaction("ReportTransaction", params);
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