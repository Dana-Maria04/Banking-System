package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

public class PrintUsers extends Command {

    public PrintUsers(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper, null, null);
    }

    @Override
    public void execute() {
        getCommandNode().put("command", getCommand().getCommand());

        ArrayNode usersOutput = getObjectMapper().createArrayNode();

        for (User user : getUsers()) {
            ObjectNode userNode = getObjectMapper().createObjectNode();
            userNode.put("firstName", user.getUser().getFirstName());
            userNode.put("lastName", user.getUser().getLastName());
            userNode.put("email", user.getUser().getEmail());

            ArrayNode accountsNode = getObjectMapper().createArrayNode();

            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = getObjectMapper().createObjectNode();
                accountNode.put("IBAN", account.getIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getAccountType());

                ArrayNode cardsNode = getObjectMapper().createArrayNode();

                if (account.getCards() != null) {
                    for (Card card : account.getCards()) {
                        ObjectNode cardNode = getObjectMapper().createObjectNode();
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

    @Override
    public void undo() {

    }
}
