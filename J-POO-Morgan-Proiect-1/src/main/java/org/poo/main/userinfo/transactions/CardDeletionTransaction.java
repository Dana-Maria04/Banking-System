package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction for deleting a card.
 * This class extends the Transaction class and stores the details specific to the Card Deletion
 * operation.
 */
@Getter
@Setter
public class CardDeletionTransaction extends Transaction {

    private final String cardNumber;
    private final String iban;

    /**
     * Constructs a CardDeletionTransaction instance with the specified parameters.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp when the transaction occurred.
     * @param email       The email of the user who initiated the transaction.
     * @param iban        The IBAN of the account from which the card is being deleted.
     * @param cardNumber  The number of the card being deleted.
     */
    public CardDeletionTransaction(final String description, final int timestamp,
                                   final String email, final String iban,
                                   final String cardNumber) {
        super(description, timestamp, email, iban);
        this.iban = iban;
        this.cardNumber = cardNumber;
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
        transactionNode.put("account", iban);
        transactionNode.put("card", cardNumber);
        transactionNode.put("cardHolder", getEmail());
    }
}
