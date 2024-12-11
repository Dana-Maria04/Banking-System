package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckCardStatusTransaction extends Transaction {
    private String cardNumber;
    private String iban;
    private String status;

    public CheckCardStatusTransaction(String description, int timestamp, String email, String cardNumber, String iban, String status) {
        super(description, timestamp);
        setEmail(email);
        this.cardNumber = cardNumber;
        this.iban = iban;
        this.status = status;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }


}
