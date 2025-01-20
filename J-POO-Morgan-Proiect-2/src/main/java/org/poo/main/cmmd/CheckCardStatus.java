package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * Command to check the card status and freeze it if the account balance
 * goes below a certain threshold. If the card is frozen, a transaction is created to
 * log this action.
 */
public class CheckCardStatus extends Command {


    private static final double BALANCE = 30.0;
    /**
     * Constructor to initialize the CheckCardStatus command with necessary parameters.
     *
     * @param users         List of users to be processed
     * @param commandNode   JSON node representing the command input
     * @param output        ArrayNode to store output results
     * @param command       Command input data
     * @param objectMapper  ObjectMapper for JSON operations
     * @param transactions  List of transactions to add generated transactions
     */
    public CheckCardStatus(final ArrayList<User> users, final ObjectNode commandNode,
                           final ArrayNode output, final CommandInput command,
                           final ObjectMapper objectMapper,
                           final ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper,
                null, transactions, null, null);
    }

    /**
     * Executes the CheckCardStatus command.
     * This method checks if the account balance is below a set value(30) and
     * freezes the card if necessary. If the card is frozen, a transaction is added.
     * @throws IllegalArgumentException if the card is not found
     */
    @Override
    public void execute() {
        for (final User user : getUsers()) {
            for (final Account account : user.getAccounts()) {
                for (final Card card : account.getAccountCards()) {
                    if (card.getCardNumber().equals(getCommand().getCardNumber())) {
                        if (account.getBalance() - account.getMinimumBalance()
                                <= BALANCE) {
                            final Map<String, Object> params = Map.of(
                                    "description", "You have reached the minimum amount of funds,"
                                            + " the card will be frozen",
                                    "timestamp", getCommand().getTimestamp(),
                                    "email", user.getUser().getEmail(),
                                    "cardNumber", card.getCardNumber(),
                                    "iban", account.getAccountIban(),
                                    "status", "frozen"
                            );

                            final Transaction transaction = CreateTransaction.getInstance()
                                    .createTransaction("CheckCardStatus", params);
                            getTransactions().add(transaction);

                            card.setFrozen(1);
                            card.setStatus("frozen");
                        }
                        return;
                    }
                }
            }
        }

        final Account outputAccount = new Account();
        outputAccount.addResponseToOutput(
                getObjectMapper(),
                getCommandNode(),
                getOutput(),
                getCommand(),
                "Card not found"
        );
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
