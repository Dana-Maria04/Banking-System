package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class ReportTransaction extends Transaction{
    private String targetIban;
    private int startTimestamp;
    private int endTimestamp;
    private Account account;
    private ArrayList<Transaction> transactions;
    private User user;


    public ReportTransaction(String description, int timestamp, String email, String targetIban,
                             int startTimestamp, int endTimestamp,
                             Account account, ArrayList<Transaction> transactions, User user) {
        super(description, timestamp, email);
        this.targetIban = targetIban;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.account = account;
        this.transactions = transactions;
        this.user = user;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        ObjectNode outputNode = transactionNode.objectNode();

        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIban());

        ArrayNode transactionsArray = transactionNode.arrayNode();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= startTimestamp &&
                    transaction.getTimestamp() <= endTimestamp &&
                    transaction.getEmail().equals(user.getUser().getEmail())) {
                ObjectNode txnNode = transactionNode.objectNode();
                transaction.addDetailsToNode(txnNode);
                transactionsArray.add(txnNode);
            }
        }

        outputNode.set("transactions", transactionsArray);

        transactionNode.set("output", outputNode);
        transactionNode.put("command", "report");
        transactionNode.put("timestamp", getTimestamp());
    }
}