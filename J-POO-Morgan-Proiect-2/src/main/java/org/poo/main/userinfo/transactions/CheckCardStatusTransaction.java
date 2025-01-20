package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for checking the status of a card.
 * This class stores details specific to the operation of checking the status of a card.
 */
public class CheckCardStatusTransaction extends Transaction {
    /**
     * Constructs a CheckCardStatusTransaction instance with the specified parameters.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp when the transaction occurred.
     * @param email       The email of the user who initiated the transaction.
     * @param iban        The IBAN associated with the transaction.
     */
    public CheckCardStatusTransaction(final String description, final int timestamp,
                                      final String email,
                                      final String iban) {
        super(description, timestamp, email, iban);
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
