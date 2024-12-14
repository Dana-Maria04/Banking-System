package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class SplitPaymentTransaction extends Transaction{
    private double amount;
    private String currency;
    private List<String> involvedAccounts;

    public SplitPaymentTransaction(String description, int timestamp, String email,
                                   double amount, String currency,
                                   List<String> involvedAccounts, String iban) {
        super(description, timestamp, email, iban);
        this.amount = amount;
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("amount", amount);
        transactionNode.put("currency", currency);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accountsArray.add(account);
        }
        transactionNode.set("involvedAccounts", accountsArray);


    }

}