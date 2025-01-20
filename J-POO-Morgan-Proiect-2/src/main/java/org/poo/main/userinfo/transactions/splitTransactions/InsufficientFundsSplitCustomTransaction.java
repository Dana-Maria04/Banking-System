package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.List;

/**
 * Represents a transaction for insufficient funds in a split payment.
 */
public class InsufficientFundsSplitCustomTransaction extends Transaction {

    private final double totalAmount;
    private final String currency;
    private final List<Double> amountForUsers;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;
    private final String error;

    private InsufficientFundsSplitCustomTransaction(Builder builder) {
        super(builder.description, builder.timestamp, builder.email, null);
        this.totalAmount = builder.totalAmount;
        this.currency = builder.currency;
        this.amountForUsers = builder.amountForUsers;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitPaymentType = builder.splitPaymentType;
        this.error = builder.error;
    }

    public static class Builder {
        private String description;
        private int timestamp;
        private String email;
        private double totalAmount;
        private String currency;
        private List<Double> amountForUsers;
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

        public Builder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setAmountForUsers(List<Double> amountForUsers) {
            this.amountForUsers = amountForUsers;
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

        public InsufficientFundsSplitCustomTransaction build() {
            return new InsufficientFundsSplitCustomTransaction(this);
        }
    }

    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        // Set description
        transactionNode.put("description", String.format("Split payment of %.2f %s", totalAmount, currency));
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("splitPaymentType", splitPaymentType);
        transactionNode.put("currency", currency);

        ObjectMapper mapper = new ObjectMapper();

        // Add amountForUsers as an array
        ArrayNode amountsArray = mapper.createArrayNode();
        for (Double amount : amountForUsers) {
            amountsArray.add(amount);
        }
        transactionNode.set("amountForUsers", amountsArray);

        // Add involvedAccounts as an array
        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accountsArray.add(account);
        }
        transactionNode.set("involvedAccounts", accountsArray);

        // Add error if it exists
        if (error != null) {
            transactionNode.put("error", error);
        }
    }
}
