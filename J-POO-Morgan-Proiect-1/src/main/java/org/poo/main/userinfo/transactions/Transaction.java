package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Transaction {
    private String description;
    private int timestamp;
    private String email;
    private String reportIban;

    public Transaction(String description, int timestamp, String email, String reportIban) {
        this.description = description;
        this.timestamp = timestamp;
        this.email = email;
        this.reportIban = reportIban;
    }

    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", description);
        transactionNode.put("timestamp", timestamp);
        if (email != null) {
            transactionNode.put("email", email);
        }
    }
}