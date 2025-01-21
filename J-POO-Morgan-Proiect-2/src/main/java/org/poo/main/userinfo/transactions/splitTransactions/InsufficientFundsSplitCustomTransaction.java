package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.List;

/**
 * Represents a transaction for insufficient funds in a custom split payment.
 */
public final class InsufficientFundsSplitCustomTransaction extends Transaction {

    private final double totalAmount;
    private final String currency;
    private final List<Double> amountForUsers;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;
    private final String error;

    /**
     * Constructs a transaction for insufficient funds in a custom split payment.
     *
     * @param builder The builder containing all necessary fields.
     */
    private InsufficientFundsSplitCustomTransaction(final Builder builder) {
        super(builder.description, builder.timestamp, builder.email, null);
        this.totalAmount = builder.totalAmount;
        this.currency = builder.currency;
        this.amountForUsers = builder.amountForUsers;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitPaymentType = builder.splitPaymentType;
        this.error = builder.error;
    }

    /**
     * Builder for creating an InsufficientFundsSplitCustomTransaction instance.
     */
    public static final class Builder {
        private String description;
        private int timestamp;
        private String email;
        private double totalAmount;
        private String currency;
        private List<Double> amountForUsers;
        private List<String> involvedAccounts;
        private String splitPaymentType;
        private String error;

        /**
         * Sets the description for the transaction.
         *
         * @param desc The description of the transaction.
         * @return The current Builder instance.
         */
        public Builder setDescription(final String desc) {
            this.description = desc;
            return this;
        }

        /**
         * Sets the timestamp for the transaction.
         *
         * @param ts The timestamp value.
         * @return The current Builder instance.
         */
        public Builder setTimestamp(final int ts) {
            this.timestamp = ts;
            return this;
        }

        /**
         * Sets the email for the transaction.
         *
         * @param userEmail The email of the user involved in the transaction.
         * @return The current Builder instance.
         */
        public Builder setEmail(final String userEmail) {
            this.email = userEmail;
            return this;
        }

        /**
         * Sets the total amount for the transaction.
         *
         * @param totalAmt The total amount involved in the transaction.
         * @return The current Builder instance.
         */
        public Builder setTotalAmount(final double totalAmt) {
            this.totalAmount = totalAmt;
            return this;
        }

        /**
         * Sets the currency for the transaction.
         *
         * @param curr The currency to be used.
         * @return The current Builder instance.
         */
        public Builder setCurrency(final String curr) {
            this.currency = curr;
            return this;
        }

        /**
         * Sets the amounts for users in the transaction.
         *
         * @param amounts The amounts to be set.
         * @return The current Builder instance.
         */
        public Builder setAmountForUsers(final List<Double> amounts) {
            this.amountForUsers = amounts;
            return this;
        }

        /**
         * Sets the involved accounts for the transaction.
         *
         * @param accounts The accounts involved in the transaction.
         * @return The current Builder instance.
         */
        public Builder setInvolvedAccounts(final List<String> accounts) {
            this.involvedAccounts = accounts;
            return this;
        }

        /**
         * Sets the split payment type.
         *
         * @param type The type of split payment.
         * @return The current Builder instance.
         */
        public Builder setSplitPaymentType(final String type) {
            this.splitPaymentType = type;
            return this;
        }

        /**
         * Sets the error for the transaction.
         *
         * @param err The error message.
         * @return The current Builder instance.
         */
        public Builder setError(final String err) {
            this.error = err;
            return this;
        }

        /**
         * Builds the InsufficientFundsSplitCustomTransaction instance.
         *
         * @return A new InsufficientFundsSplitCustomTransaction instance.
         */
        public InsufficientFundsSplitCustomTransaction build() {
            return new InsufficientFundsSplitCustomTransaction(this);
        }
    }

    /**
     * Adds transaction details to the provided ObjectNode.
     *
     * @param transactionNode The ObjectNode to populate with transaction details.
     */
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
