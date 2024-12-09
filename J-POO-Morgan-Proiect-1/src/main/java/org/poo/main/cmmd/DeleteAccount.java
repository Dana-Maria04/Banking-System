package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class DeleteAccount extends Command {

    public DeleteAccount(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                ArrayList<Account> accounts = user.getAccounts();

                boolean accountDeleted = accounts.removeIf(account -> account.getIban().equals(getCommand().getAccount()));

                if (accountDeleted) {
                    addResponseToOutput("success", "Account deleted");
                } else {
                    addResponseToOutput("error", "Account not found");
                }
                return;
            }
        }

        addResponseToOutput("error", "User not found");
    }

    @Override
    public void undo() {

    }
}
