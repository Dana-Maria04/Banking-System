package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpendingReportTransaction extends Transaction{
    private String targetIban;
    private int startTimestamp;
    private int endTimestamp;
    private Account account;
    private ArrayList<Transaction> transactions;
    private ArrayList<PayOnlineTransaction> payOnlineTransactions;
    private User user;


    public SpendingReportTransaction(String description, int timestamp, String email, String targetIban,
                                     int startTimestamp, int endTimestamp,
                                     Account account, ArrayList<Transaction> transactions, User user,
                                     String reportIban, ArrayList<PayOnlineTransaction> payOnlineTransactions) {
        super(description, timestamp, email, reportIban);
        this.targetIban = targetIban;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.account = account;
        this.transactions = transactions;
        this.user = user;
        this.payOnlineTransactions = payOnlineTransactions;
    }

    private Map<String, Double> calculateCommerciantTotals() {
        Map<String, Double> commerciantTotals = new HashMap<>();
        for (PayOnlineTransaction transaction : payOnlineTransactions) {
            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp &&
                    transaction.getReportIban().equals(targetIban)) {
                commerciantTotals.merge(transaction.getCommerciant(), transaction.getAmountPayOnline(), Double::sum);
            }
        }
        return commerciantTotals;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        ObjectNode outputNode = transactionNode.putObject("output");
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIban());


        Map<String, Double> commerciantTotals = calculateCommerciantTotals();
        ArrayNode commerciantsArray = outputNode.putArray("commerciants");
        for (Map.Entry<String, Double> entry : commerciantTotals.entrySet()) {
            ObjectNode commerciantNode = transactionNode.objectNode();
            commerciantNode.put("commerciant", entry.getKey());
            commerciantNode.put("total", entry.getValue());
            commerciantsArray.add(commerciantNode);
        }

        ArrayNode transactionsArray = outputNode.putArray("transactions");
        for (PayOnlineTransaction transaction : payOnlineTransactions) {
            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp &&
                    transaction.getReportIban().equals(targetIban)) {

                ObjectNode txnNode = transactionNode.objectNode();
                txnNode.put("amount", transaction.getAmountPayOnline());
                txnNode.put("commerciant", transaction.getCommerciant());
                txnNode.put("description", transaction.getDescription());
                txnNode.put("timestamp", transaction.getTimestamp());
                transactionsArray.add(txnNode);
            }
        }

        transactionNode.set("output", outputNode);
        transactionNode.put("command", "spendingsReport");
        transactionNode.put("timestamp", getTimestamp());
    }
}