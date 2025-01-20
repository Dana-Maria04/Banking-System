package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.List;

/**
 * Represents a transaction for insufficient funds in a split payment (equal split).
 */
public class InsufficientFundsSplitEqualTransaction extends Transaction {

    private final List<Double> amountsForUsers;
    private final String currency;
    private final double totalAmount;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;
    private final String error;

    private InsufficientFundsSplitEqualTransaction(Builder builder) {
        super(builder.description, builder.timestamp, builder.email, null);
        this.amountsForUsers = builder.amountsForUsers;
        this.currency = builder.currency;
        this.totalAmount = builder.totalAmount;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitPaymentType = builder.splitPaymentType;
        this.error = builder.error;
    }

    public static class Builder {
        private String description;
        private int timestamp;
        private String email;
        private List<Double> amountsForUsers;
        private String currency;
        private double totalAmount;
        private List<String> involvedAccounts;
        private String splitPaymentType;
        private String error;

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTimestamp(int timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAmountsForUsers(List<Double> amountsForUsers) {
            this.amountsForUsers = amountsForUsers;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setInvolvedAccounts(List<String> involvedAccounts) {
            this.involvedAccounts = involvedAccounts;
            return this;
        }

        public Builder setSplitPaymentType(String splitPaymentType) {
            this.splitPaymentType = splitPaymentType;
            return this;
        }

        public Builder setError(String error) {
            this.error = error;
            return this;
        }

        public InsufficientFundsSplitEqualTransaction build() {
            return new InsufficientFundsSplitEqualTransaction(this);
        }
    }
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        String formattedTotalAmount = String.format("%.2f", totalAmount);

        transactionNode.put("description", String.format("Split payment of %s %s", formattedTotalAmount, currency));
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("splitPaymentType", splitPaymentType);
        transactionNode.put("currency", currency);

        if (amountsForUsers != null && !amountsForUsers.isEmpty()) {
            transactionNode.put("amount", amountsForUsers.get(0));
        } else {
            transactionNode.put("amount", 0.0);
        }

        ObjectMapper mapper = new ObjectMapper();

        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accountsArray.add(account);
        }
        transactionNode.set("involvedAccounts", accountsArray);

        if (error != null) {
            transactionNode.put("error", error);
        }
    }
}

