package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.List;

/**
 * Represents a successful custom split payment transaction.
 */
public final class SplitPaymentCustomGoodTransaction extends Transaction {

    private final List<Double> amountsForUsers;
    private final String currency;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;

    /**
     * Private constructor for the SplitPaymentCustomGoodTransaction.
     *
     * @param builder The builder instance containing all required fields.
     */
    private SplitPaymentCustomGoodTransaction(final Builder builder) {
        super(builder.description, builder.timestamp, builder.email, null);
        this.amountsForUsers = builder.amountsForUsers;
        this.currency = builder.currency;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitPaymentType = builder.splitPaymentType;
    }

    /**
     * Builder class for SplitPaymentCustomGoodTransaction.
     */
    public static final class Builder {
        private String description;
        private int timestamp;
        private String email;
        private List<Double> amountsForUsers;
        private String currency;
        private List<String> involvedAccounts;
        private String splitPaymentType;

        /**
         * Sets the transaction description.
         *
         * @param desc The transaction description.
         * @return The current Builder instance.
         */
        public Builder setDescription(final String desc) {
            this.description = desc;
            return this;
        }

        /**
         * Sets the transaction timestamp.
         *
         * @param ts The transaction timestamp.
         * @return The current Builder instance.
         */
        public Builder setTimestamp(final int ts) {
            this.timestamp = ts;
            return this;
        }

        /**
         * Sets the user's email associated with the transaction.
         *
         * @param userEmail The user's email.
         * @return The current Builder instance.
         */
        public Builder setEmail(final String userEmail) {
            this.email = userEmail;
            return this;
        }

        /**
         * Sets the amounts for users involved in the transaction.
         *
         * @param amounts The amounts for users.
         * @return The current Builder instance.
         */
        public Builder setAmountsForUsers(final List<Double> amounts) {
            this.amountsForUsers = amounts;
            return this;
        }

        /**
         * Sets the transaction currency.
         *
         * @param curr The currency.
         * @return The current Builder instance.
         */
        public Builder setCurrency(final String curr) {
            this.currency = curr;
            return this;
        }

        /**
         * Sets the accounts involved in the transaction.
         *
         * @param accounts The involved accounts.
         * @return The current Builder instance.
         */
        public Builder setInvolvedAccounts(final List<String> accounts) {
            this.involvedAccounts = accounts;
            return this;
        }

        /**
         * Sets the split payment type.
         *
         * @param type The split payment type.
         * @return The current Builder instance.
         */
        public Builder setSplitPaymentType(final String type) {
            this.splitPaymentType = type;
            return this;
        }

        /**
         * Builds the SplitPaymentCustomGoodTransaction instance.
         *
         * @return A new instance of SplitPaymentCustomGoodTransaction.
         */
        public SplitPaymentCustomGoodTransaction build() {
            return new SplitPaymentCustomGoodTransaction(this);
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

        ObjectMapper mapper = new ObjectMapper();

        // Add amountsForUsers array
        ArrayNode amountsArray = mapper.createArrayNode();
        for (Double amount : amountsForUsers) {
            amountsArray.add(amount);
        }
        transactionNode.set("amountForUsers", amountsArray);

        // Add involvedAccounts array
        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accountsArray.add(account);
        }
        transactionNode.set("involvedAccounts", accountsArray);
    }
}
