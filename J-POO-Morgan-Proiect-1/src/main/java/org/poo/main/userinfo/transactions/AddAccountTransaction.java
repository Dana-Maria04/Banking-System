package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAccountTransaction extends Transaction {
    private String accountType;
    private String currency;
    private String iban;

    public AddAccountTransaction(String description, int timestamp, String email, String accountType, String currency, String iban) {
        super(description, timestamp, email, iban);
        this.accountType = accountType;
        this.currency = currency;
        this.iban = iban;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }

}