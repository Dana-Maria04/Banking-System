package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction for withdrawing savings from an account.
 * This transaction logs details about the withdrawal, including a description,
 * timestamp, and email of the user involved.
 */
@Getter
@Setter
public class WithdrawSavingsTransaction extends Transaction {

    /**
     * Constructs a WithdrawSavingsTransaction with the specified details.
     *
     * @param description A description of the transaction.
     * @param timestamp   The timestamp of when the transaction occurred.
     * @param email       The email of the user involved in the transaction.
     */
    public WithdrawSavingsTransaction(final String description,
                                      final int timestamp,
                                      final String email) {
        super(description, timestamp, email, null);
    }

    /**
     * Adds the transaction details to the provided JSON node.
     * This method is used to serialize the transaction into a JSON structure.
     *
     * @param transactionNode The ObjectNode where the transaction details will be added.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }
}
