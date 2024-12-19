package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a transaction with its associated details such as description,
 * timestamp, email, and report IBAN.It serves as the base class for various types of transactions.
 */
@Getter
@Setter
public abstract class Transaction {

    private String description;
    private int timestamp;
    private String email;
    private String reportIban;

    /**
     * Constructor to initialize a transaction with its description, timestamp, email,
     * and report IBAN.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp of the transaction.
     * @param email       The email of the user involved in the transaction.
     * @param reportIban  The IBAN associated with the report for the transaction.
     */
    public Transaction(final String description, final int timestamp, final String email,
                       final String reportIban) {
        this.description = description;
        this.timestamp = timestamp;
        this.email = email;
        this.reportIban = reportIban;
    }

    /**
     * Adds the details of the transaction.
     *
     * @param transactionNode The node to which the transaction details will be added.
     */
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", description);
        transactionNode.put("timestamp", timestamp);
        if (email != null) {
            transactionNode.put("email", email);
        }
    }
}
