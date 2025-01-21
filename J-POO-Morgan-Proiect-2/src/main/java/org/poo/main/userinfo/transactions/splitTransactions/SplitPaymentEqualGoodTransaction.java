package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.List;

/**
 * Represents a successful equal split payment transaction.
 */
public final class SplitPaymentEqualGoodTransaction extends Transaction {

    private final List<Double> amountsForUsers;
    private final String currency;
    private final double totalAmount;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;

    /**
     * Constructs a SplitPaymentEqualGoodTransaction instance.
     *
     * @param builder The builder instance used to construct the transaction.
     */
    private SplitPaymentEqualGoodTransaction(final Builder builder) {
        super(builder.description, builder.timestamp, builder.email, null);
        this.amountsForUsers = builder.amountsForUsers;
        this.currency = builder.currency;
        this.totalAmount = builder.totalAmount;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitPaymentType = builder.splitPaymentType;
    }

    /**
     * Builder for creating a SplitPaymentEqualGoodTransaction instance.
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
         * Sets the amounts for users in the transaction.
         *
         * @param amounts The amounts to be set.
         * @return The current Builder instance.
         */
        public Builder setAmountsForUsers(final List<Double> amounts) {
            this.amountsForUsers = amounts;
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
         * Builds the SplitPaymentEqualGoodTransaction instance.
         *
         * @return A new SplitPaymentEqualGoodTransaction instance.
         */
        public SplitPaymentEqualGoodTransaction build() {
            return new SplitPaymentEqualGoodTransaction(this);
        }
    }

    /**
     * Adds transaction details to the provided ObjectNode.
     *
     * @param transactionNode The ObjectNode to populate with transaction details.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("splitPaymentType", splitPaymentType);
        transactionNode.put("currency", currency);
        transactionNode.put("totalAmount", totalAmount);

        ObjectMapper mapper = new ObjectMapper();

        ArrayNode amountsArray = mapper.createArrayNode();
        for (Double amount : amountsForUsers) {
            amountsArray.add(amount);
        }
        transactionNode.set("amountForUsers", amountsArray);

        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accountsArray.add(account);
        }
        transactionNode.set("involvedAccounts", accountsArray);
    }
}
