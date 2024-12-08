package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class AddFunds implements Command{
    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;

    public AddFunds(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
    }


    @Override
    public void execute() {

        for(User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban().equals(command.getAccount())) {
                    account.setBalance(account.getBalance() + command.getAmount());
                    break;
                }
            }
        }

//        return this.users;
    }

    @Override
    public void undo() {

    }
}
