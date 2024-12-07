package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.utils.Utils;

import java.util.Arrays;


public class AddAccount implements Command{

    private User[] users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;

    public AddAccount(User[] users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
    }



    @Override
    public User[] execute() {

        for (User user : users) {
            if (user.getUser().getEmail().equals(command.getEmail())) {

                Account newAccount = new Account(Utils.generateIBAN(), command.getAccountType(), command.getCurrency());

                Account[] existingAccounts = user.getAccounts();
                Account[] updatedAccounts = Arrays.copyOf(existingAccounts, existingAccounts.length + 1);
                updatedAccounts[existingAccounts.length] = newAccount;
                user.setAccounts(updatedAccounts);

                break;
            }
        }

        return this.users;

    }

    @Override
    public void undo() {

    }
}
