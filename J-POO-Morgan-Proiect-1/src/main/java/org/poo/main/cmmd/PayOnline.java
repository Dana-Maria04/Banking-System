package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class PayOnline implements Command {
    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;

    public PayOnline(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
    }


    @Override
    public void execute() {
        for(User user : users) {
            if(user.getUser().getEmail().equals(command.getEmail())) {
                for(Account account : user.getAccounts()) {
                    account.pay(command.getAmount(), command.getCardNumber(), account.getCards());
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}
