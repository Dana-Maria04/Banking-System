package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction for a pay online operation.
 */
@Getter
@Setter
public class PayOnlineTransaction extends Transaction {

    private final double amountPayOnline;
    private final String commerciant;

    /**
     * Constructs a PayOnlineTransaction instance with the specified parameters.
     *
     * @param description   The description of the transaction.
     * @param timestamp     The timestamp when the transaction occurred.
     * @param email         The email of the user who initiated the transaction.
     * @param amountPayOnline The amount for the online payment.
     * @param commerciant   The merchant involved in the transaction.
     * @param iban          The IBAN associated with the transaction.
     */
    public PayOnlineTransaction(final String description, final int timestamp, final String email,
                                final double amountPayOnline, final String commerciant,
                                final String iban) {
        super(description, timestamp, email, iban);
        this.amountPayOnline = amountPayOnline;
        this.commerciant = commerciant;
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

        if (!getDescription().equals("Insufficient funds")) {
            transactionNode.put("amount", amountPayOnline);
            transactionNode.put("commerciant", commerciant);
        }
    }
}
