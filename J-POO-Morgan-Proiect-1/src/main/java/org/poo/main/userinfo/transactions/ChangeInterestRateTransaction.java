package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChangeInterestRateTransaction extends Transaction {
    private double newInterestRate;

    public ChangeInterestRateTransaction(String description, int timestamp, String email, String reportIban, double newInterestRate) {
        super(description, timestamp, email, reportIban);
        this.newInterestRate = newInterestRate;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }

}
