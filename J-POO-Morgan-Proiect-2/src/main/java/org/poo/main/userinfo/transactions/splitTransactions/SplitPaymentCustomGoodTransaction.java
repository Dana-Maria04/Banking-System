package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.List;

/**
 * Represents a successful custom split payment transaction.
 */
public class SplitPaymentCustomGoodTransaction extends Transaction {

    private final List<Double> amountsForUsers;
    private final String currency;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;

    private SplitPaymentCustomGoodTransaction(Builder builder) {
        super(builder.description, builder.timestamp, builder.email, null);
        this.amountsForUsers = builder.amountsForUsers;
        this.currency = builder.currency;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitPaymentType = builder.splitPaymentType;
    }

    public static class Builder {
        private String description;
        private int timestamp;
        private String email;
        private List<Double> amountsForUsers;
        private String currency;
        private List<String> involvedAccounts;
        private String splitPaymentType;

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

        public Builder setInvolvedAccounts(List<String> involvedAccounts) {
            this.involvedAccounts = involvedAccounts;
            return this;
        }

        public Builder setSplitPaymentType(String splitPaymentType) {
            this.splitPaymentType = splitPaymentType;
            return this;
        }

        public SplitPaymentCustomGoodTransaction build() {
            return new SplitPaymentCustomGoodTransaction(this);
        }
    }

    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
        transactionNode.put("splitPaymentType", splitPaymentType);
        transactionNode.put("currency", currency);

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

