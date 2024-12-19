package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a transaction that generates a spending report based on a target account,
 * date range, and transactions.
 */
public class SpendingReportTransaction extends Transaction {

    private final String targetIban;
    private final int startTimestamp;
    private final int endTimestamp;
    private final Account account;
    private final ArrayList<PayOnlineTransaction> payOnlineTransactions;

    /**
     * Constructs a SpendingReportTransaction instance with the specified parameters.
     *
     * @param description          The description of the transaction.
     * @param timestamp            The timestamp of the transaction.
     * @param email                The email of the user associated with the transaction.
     * @param targetIban           The target IBAN for the report.
     * @param startTimestamp       The start timestamp of the report's time range.
     * @param endTimestamp         The end timestamp of the report's time range.
     * @param account              The account associated with the transaction.
     * @param reportIban           The IBAN of the report's account.
     * @param payOnlineTransactions The list of pay online transactions to include in the report.
     */
    public SpendingReportTransaction(final String description, final int timestamp,
                                     final String email, final String targetIban,
                                     final int startTimestamp, final int endTimestamp,
                                     final Account account, final String reportIban,
                                     final ArrayList<PayOnlineTransaction> payOnlineTransactions) {
        super(description, timestamp, email, reportIban);
        this.targetIban = targetIban;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.account = account;
        this.payOnlineTransactions = payOnlineTransactions;
    }

    /**
     * Calculates the total spending for each merchant within the given time range and target IBAN.
     *
     * @return A map containing the merchant names as keys and their total spending as values.
     */
    private Map<String, Double> calculateCommerciantTotals() {
        Map<String, Double> commerciantTotals = new HashMap<>();
        for (PayOnlineTransaction transaction : payOnlineTransactions) {
            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp()
                    <= endTimestamp && transaction.getReportIban().equals(targetIban)) {
                commerciantTotals.merge(transaction.getCommerciant(), transaction
                        .getAmountPayOnline(), Double::sum);
            }
        }
        return new TreeMap<>(commerciantTotals);
    }

    /**
     * Adds the details of this transaction to the provided ObjectNode for output formatting.
     *
     * @param transactionNode The ObjectNode to which transaction details will be added.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        ObjectNode outputNode = transactionNode.putObject("output");
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getAccountIban());

        Map<String, Double> commerciantTotals = calculateCommerciantTotals();
        ArrayNode commerciantsArray = outputNode.putArray("commerciants");
        for (Map.Entry<String, Double> entry : commerciantTotals.entrySet()) {
            ObjectNode commerciantNode = transactionNode.objectNode();
            commerciantNode.put("commerciant", entry.getKey());
            commerciantNode.put("total", entry.getValue());
            commerciantsArray.add(commerciantNode);
        }

        ArrayNode transactionsArray = outputNode.putArray("transactions");
        for (PayOnlineTransaction transaction : payOnlineTransactions) {
            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp()
                    <= endTimestamp && transaction.getReportIban().equals(targetIban)) {

                ObjectNode txnNode = transactionNode.objectNode();
                txnNode.put("amount", transaction.getAmountPayOnline());
                txnNode.put("commerciant", transaction.getCommerciant());
                txnNode.put("description", transaction.getDescription());
                txnNode.put("timestamp", transaction.getTimestamp());
                transactionsArray.add(txnNode);
            }
        }

        transactionNode.set("output", outputNode);
        transactionNode.put("command", "spendingsReport");
        transactionNode.put("timestamp", getTimestamp());
    }
}
