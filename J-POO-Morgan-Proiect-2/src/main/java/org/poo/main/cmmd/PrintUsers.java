package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

/**
 * The PrintUsers class handles the execution of the "printUsers" command.
 * It retrieves and prints user details, including their accounts and cards.
 */
public class PrintUsers extends Command {

    /**
     * Constructs a PrintUsers command with the specified parameters.
     *
     * @param users           The list of users
     * @param commandNode     The command node containing information about the command
     * @param output          The array node to store the output
     * @param command         The command input
     * @param objectMapper    ObjectMapper for JSON operations
     */
    public PrintUsers(final ArrayList<User> users, final ObjectNode commandNode,
                      final ArrayNode output, final CommandInput command,
                      final ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper,
                null, null, null, null, null);
    }

    /**
     * Executes the printUsers command by fetching and displaying information for each user.
     * The user's name, email, accounts, and cards are included in the output.
     */
    @Override
    public void execute() {
        getCommandNode().put("command", getCommand().getCommand());

        final ArrayNode usersOutput = getObjectMapper().createArrayNode();

        for (final User user : getUsers()) {
            final ObjectNode userNode = getObjectMapper().createObjectNode();
            userNode.put("firstName", user.getUser().getFirstName());
            userNode.put("lastName", user.getUser().getLastName());
            userNode.put("email", user.getUser().getEmail());

            final ArrayNode accountsNode = getObjectMapper().createArrayNode();

            for (final Account account : user.getAccounts()) {
                final ObjectNode accountNode = getObjectMapper().createObjectNode();
                accountNode.put("IBAN", account.getAccountIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getAccountType());

                final ArrayNode cardsNode = getObjectMapper().createArrayNode();

                if (account.getAccountCards() != null) {
                    for (final Card card : account.getAccountCards()) {
                        final ObjectNode cardNode = getObjectMapper().createObjectNode();
                        cardNode.put("cardNumber", card.getCardNumber());
                        cardNode.put("status", card.getStatus());
                        cardsNode.add(cardNode);
                    }
                }

                accountNode.set("cards", cardsNode);
                accountsNode.add(accountNode);
            }

            userNode.set("accounts", accountsNode);
            usersOutput.add(userNode);
        }

        getCommandNode().set("output", usersOutput);
        getCommandNode().put("timestamp", getCommand().getTimestamp());

        getOutput().add(getCommandNode());
    }

    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
