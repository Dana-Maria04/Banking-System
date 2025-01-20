package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction for adding a new account.
 * This class extends the Transaction class and stores the details specific to
 * the AddAccount operation.
 */
@Getter
@Setter
public class AddAccountTransaction extends Transaction {

    private final String accountType;
    private final String currency;
    private final String iban;

    /**
     * Constructs an AddAccountTransaction instance with the specified parameters.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp when the transaction occurred.
     * @param email       The email of the user who initiated the transaction.
     * @param accountType The type of the account being added (e.g., "checking", "savings").
     * @param currency    The currency associated with the account.
     * @param iban        The IBAN of the newly added account.
     */
    public AddAccountTransaction(final String description, final int timestamp, final String email,
                                 final String accountType, final String currency,
                                 final String iban) {
        super(description, timestamp, email, iban);
        this.accountType = accountType;
        this.currency = currency;
        this.iban = iban;
    }

    /**
     * Adds the details of this transaction to the provided ObjectNode for output formatting.
     * @param transactionNode The ObjectNode to which transaction details will be added.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }
}
