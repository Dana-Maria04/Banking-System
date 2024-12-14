package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayOnlineTransaction extends Transaction {
    private double amountPayOnline;
    private String commerciant;

    public PayOnlineTransaction(String description, int timestamp, String email,
                                double amountPayOnline, String commerciant, String iban) {
        super(description, timestamp, email, iban);
        this.amountPayOnline = amountPayOnline;
        this.commerciant = commerciant;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());

        if (!"Insufficient funds".equals(getDescription())) {
            transactionNode.put("amount", amountPayOnline);
            transactionNode.put("commerciant", commerciant);
        }
    }


}