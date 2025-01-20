package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Represents a transaction for split payments, involving multiple accounts.
 */
public class SplitPaymentTransaction extends Transaction {

    private final double amount;
    private final String currency;
    private final List<String> involvedAccounts;
    private final String error;

    /**
     * Constructs a SplitPaymentTransaction instance with the specified parameters.
     *
     * @param description     The description of the transaction.
     * @param timestamp       The timestamp of the transaction.
     * @param email           The email of the user associated with the transaction.
     * @param amount          The amount to be split among the involved accounts.
     * @param currency        The currency of the amount being split.
     * @param involvedAccounts The list of involved accounts.
     * @param iban            The IBAN associated with the transaction.
     * @param error           Any error related to the transaction (optional).
     */
    public SplitPaymentTransaction(final String description, final int timestamp,
                                   final String email, final double amount,
                                   final String currency, final List<String> involvedAccounts,
                                   final String iban, final String error) {
        super(description, timestamp, email, iban);
        this.amount = amount;
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.error = error;
    }

    /**
     * Adds the details of the split payment transaction to the provided ObjectNode.
     *
     * @param transactionNode The ObjectNode to which the transaction details will be added.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        if (error != null) {
            transactionNode.put("error", error);
        }
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
