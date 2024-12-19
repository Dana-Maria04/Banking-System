package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction for transferring money between accounts.
 */
@Getter
@Setter
public class MoneyTransferTransaction extends Transaction {

    private final double amount;
    private final String currency;
    private final String senderIban;
    private final String receiverIban;
    private final String transferType;

    /**
     * Constructs a MoneyTransferTransaction instance with the specified parameters.
     *
     * @param description  The description of the transaction.
     * @param timestamp    The timestamp when the transaction occurred.
     * @param email        The email of the user who initiated the transaction.
     * @param amount       The amount to be transferred.
     * @param currency     The currency of the transaction.
     * @param senderIban   The IBAN of the sender.
     * @param receiverIban The IBAN of the receiver.
     * @param transferType The type of transfer (e.g., "sent", "received", "failed").
     * @param iban         The IBAN associated with the transaction.
     */
    public MoneyTransferTransaction(final String description, final int timestamp,
                                    final String email, final double amount, final String currency,
                                    final String senderIban, final String receiverIban,
                                    final String transferType, final String iban) {
        super(description, timestamp, email, iban);
        this.amount = amount;
        this.currency = currency;
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.transferType = transferType;
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

        if (!transferType.equals("failed")) {
            transactionNode.put("amount", amount + " " + currency);
            transactionNode.put("senderIBAN", senderIban);
            transactionNode.put("receiverIBAN", receiverIban);
            transactionNode.put("transferType", transferType);
        }
    }
}
