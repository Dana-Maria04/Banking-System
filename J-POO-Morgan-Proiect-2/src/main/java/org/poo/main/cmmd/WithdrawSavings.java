package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * The WithdrawSavings class is responsible for handling savings account withdrawals.
 * It ensures that the user meets specific requirements, such as having a classic account
 * and meeting the minimum age requirement, before allowing the withdrawal.
 */
public class WithdrawSavings extends Command {

    /**
     * Constructs a WithdrawSavings command.
     *
     * @param graph        The exchange graph used for currency conversion.
     * @param commandInput The input command containing withdrawal details.
     * @param transactions The list of transactions for logging purposes.
     * @param users        The list of users in the system.
     */
    public WithdrawSavings(final ExchangeGraph graph, final CommandInput commandInput,
                           final ArrayList<Transaction> transactions,
                           final ArrayList<User> users) {
        super(users, null, null, commandInput, null,
                graph, transactions, null, null, null, null);
    }

    /**
     * Executes the withdrawal command for a savings account.
     * It validates whether the user meets the requirements,
     * performs the currency conversion, updates the balances, and logs the transaction.
     * If the user does not meet the requirements, an appropriate error message is logged.
     */
    @Override
    public void execute() {
        getUsers().stream()
                .flatMap(user -> user.getAccounts().stream()
                        .map(account -> Map.entry(user, account)))
                .filter(entry ->
                        entry.getValue().getAccountIban().equals(getCommand().getAccount()))
                .forEach(entry ->
                        processAccount(entry.getKey(), entry.getValue()));
    }

    private void processAccount(final User user, final Account account) {
        if (account.getAccountType().equals("savings")) {
            if (account.verifyIfClassicAccount(user.getAccounts()) == 1) {
                handleSavingsAccount(user, account);
            } else {
                addTransaction("You do not have a classic account.", user);
            }
        }
    }

    private void handleSavingsAccount(final User user, final Account account) {
        if (user.checkOldEnough() == 1) {
            double amount = getCommand().getAmount();
            double convertedAmount = getGraph().convertCurrency(
                    amount, getCommand().getCurrency(), account.getCurrency());

            account.setBalance(account.getBalance() - convertedAmount);

            user.getAccounts().stream()
                    .filter(acc -> acc.getAccountType().equals("classic"))
                    .findFirst()
                    .ifPresent(classicAccount -> classicAccount.setBalance(
                            classicAccount.getBalance() + convertedAmount));
        } else {
            addTransaction("You don't have the minimum age required.", user);
        }
    }

    private void addTransaction(final String description, final User user) {
        Map<String, Object> params = Map.of(
                "description", description,
                "timestamp", getCommand().getTimestamp(),
                "email", user.getUser().getEmail()
        );
        Transaction transaction = CreateTransaction.getInstance()
                .createTransaction("WithdrawSavings", params);
        getTransactions().add(transaction);
    }


    /**
     * Reserved for future development to undo a savings withdrawal transaction.
     */
    @Override
    public void undo() {

    }
}
