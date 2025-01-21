package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * The DeleteAccount class handles the deletion of an account for a specified user.
 * It verifies if the account exists and whether the balance is zero before proceeding
 * with the deletion.
 * If the account has remaining funds, a transaction is logged for further processing.
 */
public class DeleteAccount extends Command {

    /**
     * Constructs a new DeleteAccount command with the specified parameters.
     *
     * @param users          The list of users
     * @param commandNode    The command node to store the output
     * @param output         The output node for storing responses
     * @param command        The command input
     * @param objectMapper   ObjectMapper for JSON operations
     * @param transactions   The list of transactions to log the action
     */
    public DeleteAccount(final ArrayList<User> users, final ObjectNode commandNode,
                         final ArrayNode output, final CommandInput command,
                         final ObjectMapper objectMapper,
                         final ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper,
                null, transactions, null, null, null, null);
    }

    /**
     * Executes the DeleteAccount command by verifying if the account exists and if
     * the balance is zero. If the balance is zero, the account is removed from the user's
     * accounts. If funds remain, a transaction is logged.
     */
    @Override
    public void execute() {
        for (final User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                final ArrayList<Account> accounts = user.getAccounts();
                Account targetAccount = null;
                for (final Account acc : accounts) {
                    if (acc.getAccountIban().equals(getCommand().getAccount())) {
                        targetAccount = acc;
                        break;
                    }
                }

                if (targetAccount == null) {
                    return;
                }

                if (targetAccount.getBalance() == 0) {
                    accounts.remove(targetAccount);
                    addResponseToOutput("success", "Account deleted");
                } else {
                    addResponseToOutput("error",
                            "Account couldn't be deleted - see org.poo.transactions for details");

                    final Map<String, Object> params = Map.of(
                            "description",
                            "Account couldn't be deleted - there are funds remaining",
                            "timestamp", getCommand().getTimestamp(),
                            "email", user.getUser().getEmail(),
                            "iban", targetAccount.getAccountIban()
                    );

                    final Transaction transaction = CreateTransaction.getInstance()
                            .createTransaction("DeleteAccount", params);

                    getTransactions().add(transaction);
                }

                return;
            }
        }

        addResponseToOutput("error", "User not found");
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
