package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyTransferTransaction extends Transaction {
    private double amount;
    private String currency;
    private String senderIban;
    private String receiverIban;
    private String transferType;

    public MoneyTransferTransaction(String description, int timestamp, double amount, String currency,
                                    String senderIban, String receiverIban, String transferType) {
        super(description, timestamp);
        this.amount = amount;
        this.currency = currency;
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.transferType = transferType;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());

        if (!"failed".equals(transferType)) {
            transactionNode.put("amount", amount + " " + currency);
            transactionNode.put("senderIBAN", senderIban);
            transactionNode.put("receiverIBAN", receiverIban);
            transactionNode.put("transferType", transferType);
        }
    }
}
