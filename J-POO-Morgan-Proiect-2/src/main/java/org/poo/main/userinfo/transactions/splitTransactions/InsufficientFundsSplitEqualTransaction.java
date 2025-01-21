package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.List;

/**
 * Represents a transaction for insufficient funds in a split payment (equal split).
 */
public final class InsufficientFundsSplitEqualTransaction extends Transaction {

    private final List<Double> amountsForUsers;
    private final String currency;
    private final double totalAmount;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;
    private final String error;

    private InsufficientFundsSplitEqualTransaction(final Builder builder) {
        super(builder.description, builder.timestamp, builder.email, null);
        this.amountsForUsers = builder.amountsForUsers;
        this.currency = builder.currency;
        this.totalAmount = builder.totalAmount;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitPaymentType = builder.splitPaymentType;
        this.error = builder.error;
    }

    /**
     * Builder for creating an InsufficientFundsSplitEqualTransaction.
     */
    public static final class Builder {
        private String description;
        private int timestamp;
        private String email;
        private List<Double> amountsForUsers;
        private String currency;
        private double totalAmount;
        private List<String> involvedAccounts;
        private String splitPaymentType;
        private String error;

        /**
         * Sets the description for the transaction.
         * @param description The description to set.
         * @return The current Builder instance.
         */
        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the timestamp for the transaction.
         * @param timestampValue The timestamp value.
         * @return The current Builder instance.
         */
        public Builder setTimestamp(final int timestampValue) {
            this.timestamp = timestampValue;
            return this;
        }

        /**
         * Sets the email for the transaction.
         * @param emailValue The email of the user.
         * @return The current Builder instance.
         */
        public Builder setEmail(final String emailValue) {
            this.email = emailValue;
            return this;
        }

        /**
         * Sets the amounts for users.
         * @param amounts The amounts to set.
         * @return The current Builder instance.
         */
        public Builder setAmountsForUsers(final List<Double> amounts) {
            this.amountsForUsers = amounts;
            return this;
        }

        /**
         * Sets the currency for the transaction.
         * @param currencyValue The currency to set.
         * @return The current Builder instance.
         */
        public Builder setCurrency(final String currencyValue) {
            this.currency = currencyValue;
            return this;
        }

        /**
         * Sets the total amount for the transaction.
         * @param totalValue The total amount.
         * @return The current Builder instance.
         */
        public Builder setTotalAmount(final double totalValue) {
            this.totalAmount = totalValue;
            return this;
        }

        /**
         * Sets the involved accounts for the transaction.
         * @param accounts The involved accounts.
         * @return The current Builder instance.
         */
        public Builder setInvolvedAccounts(final List<String> accounts) {
            this.involvedAccounts = accounts;
            return this;
        }

        /**
         * Sets the split payment type.
         * @param type The split payment type.
         * @return The current Builder instance.
         */
        public Builder setSplitPaymentType(final String type) {
            this.splitPaymentType = type;
            return this;
        }

        /**
         * Sets the error message for the transaction.
         * @param errorMessage The error message.
         * @return The current Builder instance.
         */
        public Builder setError(final String errorMessage) {
            this.error = errorMessage;
            return this;
        }

        /**
         * Builds the InsufficientFundsSplitEqualTransaction.
         * @return The built transaction instance.
         */
        public InsufficientFundsSplitEqualTransaction build() {
            return new InsufficientFundsSplitEqualTransaction(this);
        }
    }

    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        String formattedTotalAmount = String.format("%.2f", totalAmount);

        transactionNode.put(
                "description",
                String.format("Split payment of %s %s", formattedTotalAmount, currency)
        );
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
