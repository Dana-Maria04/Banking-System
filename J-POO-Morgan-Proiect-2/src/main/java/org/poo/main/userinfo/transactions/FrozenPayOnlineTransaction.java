package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction for freezing a PayOnline transaction.
 */
@Getter
@Setter
public class FrozenPayOnlineTransaction extends Transaction {

    /**
     * Constructs a FrozenPayOnlineTransaction instance with the specified parameters.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp when the transaction occurred.
     * @param email       The email of the user who initiated the transaction.
     * @param reportIban  The IBAN associated with the transaction report.
     */
    public FrozenPayOnlineTransaction(final String description, final int timestamp,
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
