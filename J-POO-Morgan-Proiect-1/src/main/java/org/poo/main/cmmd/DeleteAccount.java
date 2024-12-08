package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class DeleteAccount implements Command {
    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;

    public DeleteAccount(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute() {
        for (User user : users) {
            if (user.getUser().getEmail().equals(command.getEmail())) {
                ArrayList<Account> accounts = user.getAccounts();

                boolean accountDeleted = accounts.removeIf(account -> account.getIban().equals(command.getAccount()));

                ObjectNode responseNode = objectMapper.createObjectNode();
                if (accountDeleted) {
                    responseNode.put("success", "Account deleted");
                } else {
                    responseNode.put("error", "Account not found");
                }
                responseNode.put("timestamp", command.getTimestamp());

                commandNode.put("command", command.getCommand());
                commandNode.set("output", responseNode);
                commandNode.put("timestamp", command.getTimestamp());
                output.add(commandNode);

                return;
            }
        }

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("error", "User not found");
        responseNode.put("timestamp", command.getTimestamp());
        commandNode.set("output", responseNode);
        commandNode.put("timestamp", command.getTimestamp());
        output.add(commandNode);
    }

    @Override
    public void undo() {

    }
}
