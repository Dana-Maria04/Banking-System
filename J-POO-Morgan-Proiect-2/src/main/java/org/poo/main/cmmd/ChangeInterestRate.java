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
 * Command to change the interest rate of an account.
 * This command checks if the account is a savings account and applies the new interest rate.
 * If the account is not a savings account, it adds a response to the output.
 */
public class ChangeInterestRate extends Command {

    /**
     * Constructor to initialize the ChangeInterestRate command with necessary parameters.
     *
     * @param users         List of users to be processed
     * @param commandNode   JSON node representing the command input
     * @param output        ArrayNode to store output results
     * @param command       Command input data
     * @param objectMapper  ObjectMapper for JSON operations
     * @param transactions  List of transactions to add generated transactions
     */
    public ChangeInterestRate(final ArrayList<User> users, final ObjectNode commandNode,
                              final ArrayNode output, final CommandInput command,
                              final ObjectMapper objectMapper,
                              final ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, null);
    }

    /**
     * Executes the ChangeInterestRate command.
     * This method checks whether the account is of type "savings" and updates the interest
     * rate if it is.
     * If the account is not a savings account, it adds a response to the output.
     *
     * @throws IllegalArgumentException if the account with the given IBAN is not found
     */
    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(getCommand().getAccount())) {

                    if (account.getAccountType().equals("classic")) {
                        account.addResponseToOutput(
                                getObjectMapper(),
                                getCommandNode(),
                                getOutput(),
                                getCommand(),
                                "This is not a savings account"
                        );
                        return;
                    }

                    account.setInterestRate(getCommand().getInterestRate());
                    Map<String, Object> params = Map.of(
                            "description", "Interest rate of the account changed to "
                                    + getCommand().getInterestRate(),
                            "timestamp", getCommand().getTimestamp(),
                            "email", user.getUser().getEmail(),
                            "iban", account.getAccountIban(),
                            "interestRate", getCommand().getInterestRate()
                    );

                    Transaction transaction = CreateTransaction.getInstance().
                            createTransaction("ChangeInterestRate", params);

                    getTransactions().add(transaction);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Account not found");
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
