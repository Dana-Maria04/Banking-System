package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for insufficient funds.
 */
public class InsufficientFundsTransaction extends Transaction {

    /**
     * Constructs an InsufficientFundsTransaction instance with the specified parameters.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp when the transaction occurred.
     * @param email       The email of the user associated with the transaction (optional).
     */
    public InsufficientFundsTransaction(final String description, final int timestamp, final String email) {
        super(description, timestamp, email, null);
    }

    /**
     * Adds the details of this transaction to the provided ObjectNode for output formatting.
     *
     * @param transactionNode The ObjectNode to which transaction details will be added.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }
}
