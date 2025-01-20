package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.SplitPaymentTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The SplitPayment class is responsible for processing a split payment across multiple accounts,
 * taking into account conversion rates, balances, and minimum balance constraints.
 */
public class SplitPayment extends Command {

    /**
     * Constructs a SplitPayment command with the provided parameters.
     *
     * @param graph                     The ExchangeGraph for currency conversion.
     * @param users                     The list of users involved in the split payment.
     * @param commandNode               The command node containing the command details.
     * @param output                    The output node where the response will be written.
     * @param command                   The command input containing parameters for split payment.
     * @param objectMapper              ObjectMapper for JSON operations
     * @param transactions              The list of transactions associated with the users.
     */
    public SplitPayment(final ExchangeGraph graph, final ArrayList<User> users,
                        final ObjectNode commandNode, final ArrayNode output,
                        final CommandInput command, final ObjectMapper objectMapper,
                        final ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, graph,
                transactions, null, null);
    }

    /**
     * Executes the split payment command. It splits the amount across multiple accounts
     * and checks if any account has insufficient funds to complete the transaction.
     */
    @Override
    public void execute() {
        final List<String> accounts = getCommand().getAccounts();
        final int rate = accounts.size();
        final double amountForEach = getCommand().getAmount() / rate;
        final String totalDescription = String.format("Split payment of %.2f %s",
                getCommand().getAmount(), getCommand().getCurrency());

        Account errorAccount = new Account();
        boolean insufficientFunds = false;
        for (final String iban : accounts) {
            for (final User user : getUsers()) {
                for (final Account account : user.getAccounts()) {
                    if (account.getAccountIban().equals(iban)) {
                        final double convertedAmount = getGraph().convertCurrency(amountForEach,
                                getCommand().getCurrency(), account.getCurrency());
                        if (account.getMinimumBalance() > account.getBalance() - convertedAmount) {
                            errorAccount = account;
                            insufficientFunds = true;
                        }
                    }
                }
            }
        }

        if (insufficientFunds) {
            processTransactions(accounts, amountForEach, totalDescription, 1,
                    errorAccount.getAccountIban());
        } else {
            processTransactions(accounts, amountForEach, totalDescription, 0,
                    null);
        }
    }

    /**
     * Processes transactions for the split payment. It handles both successful transactions
     * and transactions with errors.
     *
     * @param accounts           The list of account IBANs to be processed.
     * @param amountForEach      The amount each account will receive (or pay).
     * @param description        The description of the transaction.
     * @param errorFlag          A flag indicating whether the transaction failed (1) or was
     *                           successful (0).
     * @param errorAccountIban   The IBAN of the account that caused the error (if any).
     */
    private void processTransactions(final List<String> accounts, final double amountForEach,
                                     final String description, final int errorFlag,
                                     final String errorAccountIban) {
        for (final String iban : accounts) {
            for (final User user : getUsers()) {
                for (final Account account : user.getAccounts()) {
                    if (account.getAccountIban().equals(iban)) {
                        final double convertedAmount = getGraph().convertCurrency(amountForEach,
                                getCommand().getCurrency(), account.getCurrency());
                        Map<String, Object> params;
                        if (errorFlag == 1) {
                            params = constructParams(
                                    description,
                                    Map.of(
                                            "amount", amountForEach,
                                            "currency", getCommand().getCurrency(),
                                            "involvedAccounts", accounts,
                                            "iban", account.getAccountIban(),
                                            "error", "Account " + errorAccountIban
                                                    + " has insufficient funds for"
                                                    + " a split payment.",
                                            "email", user.getUser().getEmail(),
                                            "timestamp", getCommand().getTimestamp()
                                    )
                            );
                        } else {
                            params = constructParams(
                                    description,
                                    Map.of(
                                            "amount", amountForEach,
                                            "currency", getCommand().getCurrency(),
                                            "involvedAccounts", accounts,
                                            "iban", account.getAccountIban(),
                                            "email", user.getUser().getEmail(),
                                            "timestamp", getCommand().getTimestamp()
                                    )
                            );
                            account.setBalance(account.getBalance() - convertedAmount);
                        }

                        SplitPaymentTransaction transaction = (SplitPaymentTransaction)
                                CreateTransaction.getInstance()
                                        .createTransaction("SplitPayment", params);
                        getTransactions().add(transaction);
                    }
                }
            }
        }
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
