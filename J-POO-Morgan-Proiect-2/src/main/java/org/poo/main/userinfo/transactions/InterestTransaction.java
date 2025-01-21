package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents an interest transaction for an account.
 * This class stores the interest income details such as amount, currency,
 * and associated metadata like description, timestamp, and email.
 */
public class InterestTransaction extends Transaction {
    private final double income;
    private final String currency;

    /**
     * Constructs an InterestTransaction with the specified details.
     *
     * @param income      The interest income amount.
     * @param currency    The currency of the income.
     * @param description A description of the transaction.
     * @param timestamp   The timestamp of the transaction.
     * @param email       The email of the user involved in the transaction.
     */
    public InterestTransaction(final double income, final String currency, final String description,
                               final int timestamp, final String email) {
        super(description, timestamp, email, null);
        this.income = income;
        this.currency = currency;
    }

    /**
     * Gets the interest income amount.
     *
     * @return The interest income as a double.
     */
    public double getIncome() {
        return income;
    }

    /**
     * Gets the currency of the interest income.
     *
     * @return The currency as a string.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Adds details of the interest transaction to the given JSON node.
     * This method populates the JSON node with the transaction's description,
     * timestamp, income amount, and currency.
     *
     * @param transactionNode The ObjectNode to which details will be added.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("amount", income);
        transactionNode.put("currency", currency);
    }
}
