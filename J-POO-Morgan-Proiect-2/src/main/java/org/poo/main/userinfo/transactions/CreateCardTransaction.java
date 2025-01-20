package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for creating a new card.
 * This class stores details specific to the operation of creating a new card.
 */
public class CreateCardTransaction extends Transaction {

    private final String account;
    private final String cardNumber;
    private final String cardHolder;

    /**
     * Constructs a CreateCardTransaction instance with the specified parameters.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp when the transaction occurred.
     * @param email       The email of the user who initiated the transaction.
     * @param account     The account associated with the new card.
     * @param cardNumber  The card number associated with the new card.
     * @param cardHolder  The holder of the new card.
     * @param reportIban  The IBAN associated with the report.
     */
    public CreateCardTransaction(final String description, final int timestamp, final String email,
                                 final String account, final String cardNumber,
                                 final String cardHolder, final String reportIban) {
        super(description, timestamp, email, reportIban);
        this.account = account;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
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
        transactionNode.put("account", account);
        transactionNode.put("card", cardNumber);
        transactionNode.put("cardHolder", cardHolder);
    }
}
