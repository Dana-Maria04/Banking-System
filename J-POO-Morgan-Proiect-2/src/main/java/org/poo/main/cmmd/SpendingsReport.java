package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.SpendingReportTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * Generates a report for a specific account's
 * spending activity within a given time period. It is executed based on the provided account IBAN,
 * start and end timestamps.
 */
public class SpendingsReport extends Command {

    /**
     * Constructs a SpendingsReport command with the provided parameters.
     *
     * @param users                     The list of users.
     * @param commandNode               The command node with the JSON structure for the command.
     * @param output                    The output node where the response will be written.
     * @param command                   The command input containing the parameters for the command
     * @param objectMapper              ObjectMapper for JSON operations
     * @param transactions              The list of transactions associated with the users.
     * @param payOnlineTransactions     The list of PayOnlineTransactions related to the report.
     */
    public SpendingsReport(final ArrayList<User> users, final ObjectNode commandNode,
                           final ArrayNode output, final CommandInput command,
                           final ObjectMapper objectMapper,
                           final ArrayList<Transaction> transactions,
                           final ArrayList<PayOnlineTransaction> payOnlineTransactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions,
                payOnlineTransactions, null);
    }

    /**
     * Executes the SpendingsReport command. It retrieves the account by IBAN, checks if it's
     * a savings account, and generates the spending report based on the provided start and end
     * timestamps.
     *
     * @throws IllegalArgumentException if the account is not found.
     */
    @Override
    public void execute() {
        final String targetIban = getCommand().getAccount();
        final int startTimestamp = getCommand().getStartTimestamp();
        final int endTimestamp = getCommand().getEndTimestamp();

        for (final User user : getUsers()) {
            for (final Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(targetIban)) {
                    if (account.getAccountType().equals("savings")) {
                        final ObjectNode errorNode = getObjectMapper().createObjectNode();
                        errorNode.put("command", getCommand().getCommand());
                        final ObjectNode outputNode = errorNode.putObject("output");
                        outputNode.put("error", "This kind of report is not supported"
                                + " for a saving account");
                        errorNode.put("timestamp", getCommand().getTimestamp());
                        getOutput().add(errorNode);
                        return;
                    }

                    final Map<String, Object> params = constructParams(
                            getCommand().getDescription(),
                            Map.of(
                                "targetIban", targetIban,
                                "startTimestamp", startTimestamp,
                                "endTimestamp", endTimestamp,
                                "account", account,
                                "transactions", getTransactions(),
                                "user", user,
                                "reportIban", account.getAccountIban(),
                                "payOnlineTransactions", getSpendingsReportTransactions()
                            )
                    );

                    final SpendingReportTransaction spendingTransaction =
                            (SpendingReportTransaction) CreateTransaction.getInstance()
                                    .createTransaction("SpendingsReport", params);

                    final ObjectNode transactionNode = getObjectMapper().createObjectNode();
                    spendingTransaction.addDetailsToNode(transactionNode);
                    getOutput().add(transactionNode);
                    return;
                }
            }
        }

        final Account tempAccount = new Account();
        tempAccount.addResponseToOutput(
                getObjectMapper(),
                getCommandNode(),
                getOutput(),
                getCommand(),
                "Account not found"
        );
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
