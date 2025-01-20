package org.poo.main.userinfo.transactions;

public class InterestTransaction extends Transaction {
    private final double income;
    private final String currency;

    /**
     * Constructor for InterestTransaction.
     *
     * @param income      The interest income amount.
     * @param currency    The currency of the income.
     * @param description A description of the transaction.
     * @param timestamp   The timestamp of the transaction.
     * @param email       The email of the user involved in the transaction.
     */
    public InterestTransaction(double income, String currency, String description, int timestamp, String email) {
        super(description, timestamp, email, null);
        this.income = income;
        this.currency = currency;
    }

    public double getIncome() {
        return income;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public void addDetailsToNode(final com.fasterxml.jackson.databind.node.ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("amount", income);
        transactionNode.put("currency", currency);
    }
}
