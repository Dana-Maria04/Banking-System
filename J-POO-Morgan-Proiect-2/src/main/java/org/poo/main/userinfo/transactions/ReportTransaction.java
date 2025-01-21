package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

/**
 * Represents a transaction for generating a report.
 */
public class ReportTransaction extends Transaction {

    private final String targetIban;
    private final int startTimestamp;
    private final int endTimestamp;
    private final Account account;
    private final ArrayList<Transaction> transactions;
    private final User user;

    /**
     * Constructs a ReportTransaction instance with the specified parameters.
     *
     * @param description   The description of the transaction.
     * @param timestamp     The timestamp when the transaction occurred.
     * @param email         The email of the user who initiated the transaction.
     * @param targetIban    The IBAN associated with the target account for the report.
     * @param startTimestamp The starting timestamp for the report range.
     * @param endTimestamp   The ending timestamp for the report range.
     * @param account        The account related to the report.
     * @param transactions   The list of transactions to include in the report.
     * @param user           The user who initiated the report.
     * @param reportIban     The IBAN associated with the report.
     */
    public ReportTransaction(final String description, final int timestamp, final String email,
                             final String targetIban, final int startTimestamp,
                             final int endTimestamp, final Account account,
                             final ArrayList<Transaction> transactions, final User user,
                             final String reportIban) {
        super(description, timestamp, email, reportIban);
        this.targetIban = targetIban;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.account = account;
        this.transactions = transactions;
        this.user = user;
    }

    /**
     * Adds the details of this transaction to the provided ObjectNode for output formatting.
     *
     * @param transactionNode The ObjectNode to which transaction details will be added.
     */
    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        ObjectNode outputNode = transactionNode.objectNode();

        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getAccountIban());

        ArrayNode transactionsArray = transactionNode.arrayNode();
        for (Transaction transaction : transactions) {

            if (transaction.getReportIban() == null) {
                return;
            }

            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp
                    && transaction.getEmail().equals(user.getUser().getEmail())
                    && transaction.getReportIban().equals(targetIban)) {
                ObjectNode txnNode = transactionNode.objectNode();
                transaction.addDetailsToNode(txnNode);
                transactionsArray.add(txnNode);
            }
        }

        outputNode.set("transactions", transactionsArray);
        transactionNode.set("output", outputNode);
        transactionNode.put("command", "report");
        transactionNode.put("timestamp", getTimestamp());
    }
}
