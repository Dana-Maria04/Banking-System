package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class PrintUsers implements Command{
    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;

    public PrintUsers(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute() {
        commandNode.put("command", command.getCommand());

        ArrayNode usersOutput = objectMapper.createArrayNode();

        for (User user : users) {
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getUser().getFirstName());
            userNode.put("lastName", user.getUser().getLastName());
            userNode.put("email", user.getUser().getEmail());

            ArrayNode accountsNode = objectMapper.createArrayNode();

            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("IBAN", account.getIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getAccountType());

                ArrayNode cardsNode = objectMapper.createArrayNode();

                if (account.getCards() != null) {
                    for (Card card : account.getCards()) {
                        ObjectNode cardNode = objectMapper.createObjectNode();
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
        commandNode.set("output", usersOutput);
        commandNode.put("timestamp", command.getTimestamp());

        output.add(commandNode);

//        return users;
    }


    @Override
    public void undo() {

    }
}
