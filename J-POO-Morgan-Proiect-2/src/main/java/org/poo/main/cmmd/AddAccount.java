package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Adds a new account for a user.
 * This command processes the creation of a new account, adding it to the user
 * and generating a transaction for the account creation.
 */
public class AddAccount extends Command {

    /**
     * Constructor to initialize the AddAccount command with necessary parameters.
     *
     * @param users         List of users to be processed
     * @param commandNode   JSON node representing the command input
     * @param output        ArrayNode to store output results
     * @param command       Command input data
     * @param objectMapper  ObjectMapper for JSON operations
     * @param transactions  List of transactions to be updated
     */
    public AddAccount(final ArrayList<User> users, final ObjectNode commandNode,
                      final ArrayNode output, final CommandInput command,
                      final ObjectMapper objectMapper, final ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null,
                transactions, null, null);
    }

    /**
     * Executes the AddAccount command.
     * This method creates a new account for the user identified by the email in the command input,
     * initializes it with specified values, adds it to the user's list of accounts, and creates
     * a transaction for the account creation.
     */
    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {

                Account newAccount = new Account(Utils.generateIBAN(), getCommand()
                        .getAccountType(),
                        getCommand().getCurrency(), 0, new ArrayList<>());
                newAccount.setAccountCards(new ArrayList<>());
                newAccount.setInterestRate(getCommand().getInterestRate());

                user.getAccounts().add(newAccount);

                Map<String, Object> additionalParams = Map.of(
                    "accountType", getCommand().getAccountType(),
                    "currency", getCommand().getCurrency(),
                    "iban", newAccount.getAccountIban()
                );

                Map<String, Object> params = constructParams("New account created",
                        additionalParams);

                Transaction transaction = CreateTransaction.getInstance()
                                            .createTransaction("AddAccount", params);
                getTransactions().add(transaction);

                break;
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
