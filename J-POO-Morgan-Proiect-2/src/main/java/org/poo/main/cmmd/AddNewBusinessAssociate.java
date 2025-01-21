package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.BusinessAccount;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import java.util.ArrayList;

public class AddNewBusinessAssociate extends Command {

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
    public AddNewBusinessAssociate(final ArrayList<User> users, final ObjectNode commandNode,
                      final ArrayNode output, final CommandInput command,
                      final ObjectMapper objectMapper, final ArrayList<Transaction> transactions,
                       final ArrayList<BusinessAccount> businessAccounts) {
        super(users, commandNode, output, command, objectMapper, null,
                transactions, null, null, null, businessAccounts);
    }

    /**
     * Executes the AddAccount command.
     * This method creates a new account for the user identified by the email in the command input,
     * initializes it with specified values, adds it to the user's list of accounts, and creates
     * a transaction for the account creation.
     */
    @Override
    public void execute() {
        for (BusinessAccount account : getBusinessAccounts()) {
            if (account.getAccountIban().equals(getCommand().getAccount())) {
                if (getCommand().getRole().equals("employee")) {
                    for (User user : getUsers()) {
                        if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                            if (!account.getEmployees().contains(user)) {
                                account.getEmployees().add(user);
                            }
                            return;
                        }
                    }
                } else if (getCommand().getRole().equals("manager")) {
                    for (User user : getUsers()) {
                        if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                            if (!account.getManagers().contains(user)) {
                                account.getManagers().add(user);
                            }
                            return;
                        }
                    }
                }
                return;
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
