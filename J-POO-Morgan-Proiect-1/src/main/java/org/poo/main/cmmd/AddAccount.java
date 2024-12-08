package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;


public class AddAccount implements Command{

    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;

    public AddAccount(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
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

                Account newAccount = new Account(Utils.generateIBAN(), command.getAccountType(), command.getCurrency(),
                                            0);
                newAccount.setCards(new ArrayList<>());
                ArrayList<Account> existingAccounts = user.getAccounts();



                existingAccounts.add(newAccount);
                user.setAccounts(existingAccounts);
                break;
            }
        }

//        return this.users;

    }

    @Override
    public void undo() {

    }
}
