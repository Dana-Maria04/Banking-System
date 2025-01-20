package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashWithdrawalTransaction extends Transaction {
    private final String iban;
    private final Double amount; // Nullable for error cases
    private final String currency;

    /**
     * Constructor for transactions with all fields.
     *
     * @param description The description of the transaction.
     * @param timestamp   The timestamp of the transaction.
     * @param email       The email of the user.
     * @param iban        The IBAN of the account.
     * @param amount      The amount involved in the transaction.
     * @param currency    The currency of the transaction.
     */
    public CashWithdrawalTransaction(String description, int timestamp, String email,
                                     String iban, Double amount, String currency) {
        super(description, timestamp, email, null);
        this.iban = iban;
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Constructor for error transactions.
     *
     * @param description The description of the error.
     * @param timestamp   The timestamp of the transaction.
     */
    public CashWithdrawalTransaction(String description, int timestamp, String email) {
        super(description, timestamp, email, null);
        this.iban = null;
        this.amount = null;
        this.currency = null;
    }

    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        if (amount != null) {
            transactionNode.put("amount", amount);
        }
    }
}
