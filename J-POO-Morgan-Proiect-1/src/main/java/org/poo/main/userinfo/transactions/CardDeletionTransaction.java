package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDeletionTransaction extends Transaction {
    private String cardNumber;
    private String iban;

    public CardDeletionTransaction(String description, int timestamp, String email, String iban, String cardNumber) {
        super(description, timestamp);
        setEmail(email);
        this.iban = iban;
        this.cardNumber = cardNumber;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("account", iban);
        transactionNode.put("card", cardNumber);
        transactionNode.put("cardHolder", getEmail());
    }

}
