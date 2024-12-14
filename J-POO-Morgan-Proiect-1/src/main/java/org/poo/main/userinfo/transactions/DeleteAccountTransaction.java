package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class DeleteAccountTransaction extends Transaction {
    public DeleteAccountTransaction(String description, int timestamp, String email, String iban) {
        super(description, timestamp, email, iban);
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());

    }
}
