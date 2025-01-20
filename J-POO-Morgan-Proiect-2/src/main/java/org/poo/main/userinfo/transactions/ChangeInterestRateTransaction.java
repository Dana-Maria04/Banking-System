package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for changing the interest rate of an account.
 * This class extends the Transaction class and stores the details specific to the Change
 * Interest Rate operation.
 */
public class ChangeInterestRateTransaction extends Transaction {

    /**
     * Constructs a ChangeInterestRateTransaction instance with the specified parameters.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp when the transaction occurred.
     * @param email       The email of the user who initiated the transaction.
     * @param reportIban  The IBAN of the account for which the interest rate is being changed.
     */
    public ChangeInterestRateTransaction(final String description, final int timestamp,
                                         final String email, final String reportIban) {
        super(description, timestamp, email, reportIban);
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
