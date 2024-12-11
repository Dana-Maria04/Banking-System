package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCardTransaction extends Transaction {
    private String account;
    private String cardNumber;
    private String cardHolder;

    public CreateCardTransaction(String description, int timestamp, String email, String account, String cardNumber, String cardHolder) {
        super(description, timestamp);
        setEmail(email);
        this.account = account;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("account", account);
        transactionNode.put("card", cardNumber);
        transactionNode.put("cardHolder", cardHolder);
    }

}
