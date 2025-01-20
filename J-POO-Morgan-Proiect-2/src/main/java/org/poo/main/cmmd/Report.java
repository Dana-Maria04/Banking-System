package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.ReportTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * The Report class handles the execution of the "report" command. It retrieves
 * information about a specific account and generates a report based on the
 * provided time range and associated transactions.
 */
public class Report extends Command {

    /**
     * Constructs a Report command with the specified parameters.
     *
     * @param users                The list of users
     * @param command              The command input
     * @param exchangeGraph        The exchange rate graph for currency conversions
     * @param output               The array node to store the output
     * @param objectMapper         ObjectMapper for JSON operations
     * @param commandNode          The command node containing information about the command
     * @param transactions         The list of transactions
     */
    public Report(final ArrayList<User> users, final CommandInput command,
                  final ExchangeGraph exchangeGraph, final ArrayNode output,
                  final ObjectMapper objectMapper, final ObjectNode commandNode,
                  final ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, exchangeGraph, transactions,
                null, null, null);
    }

    /**
     * Executes the "report" command. It retrieves the account based on the provided
     * IBAN and generates a report including the account's details and associated transactions
     * within the specified time range.
     */
    @Override
    public void execute() {
        final String targetIban = getCommand().getAccount();
        final int startTimestamp = getCommand().getStartTimestamp();
        final int endTimestamp = getCommand().getEndTimestamp();

        Account foundAccount = new Account();
        User associatedUser = new User();

        boolean accountFound = false;

        for (final User user : getUsers()) {
            for (final Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(targetIban)) {
                    foundAccount = account;
                    associatedUser = user;
                    accountFound = true;
                    break;
                }
            }
            if (accountFound) {
                break;
            }
        }

        if (!accountFound) {
            foundAccount.addResponseToOutput(getObjectMapper(), getCommandNode(), getOutput(),
                    getCommand(), "Account not found");
            return;
        }

        final Map<String, Object> params = constructParams(getCommand().getDescription(), Map.of(
                "targetIban", targetIban,
                "startTimestamp", startTimestamp,
                "endTimestamp", endTimestamp,
                "account", foundAccount,
                "transactions", new ArrayList<>(getTransactions()),
                "user", associatedUser,
                "reportIban", foundAccount.getAccountIban()
        ));

        final ReportTransaction reportTransaction = (ReportTransaction) CreateTransaction
                .getInstance()
                .createTransaction("ReportTransaction", params);

        final ObjectNode transactionNode = getObjectMapper().createObjectNode();
        reportTransaction.addDetailsToNode(transactionNode);
        getOutput().add(transactionNode);
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
