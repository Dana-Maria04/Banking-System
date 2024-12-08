package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class CreateCard implements Command {
    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;
    private int oneTime;

    public CreateCard(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                      ObjectMapper objectMapper, int oneTime) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
        this.oneTime = oneTime;
    }




    @Override
    public void execute() {

        for(User user : users) {
            if(user.getUser().getEmail().equals(command.getEmail())) {
                for(Account account : user.getAccounts()) {

                    if(account.getIban().equals(command.getAccount())) {


                        Card newCard = new Card(Utils.generateCardNumber(), "active", oneTime);
                        account.getCards().add(newCard);
                        break;
                    }
                }
            }
        }

//        return this.users;
    }

    @Override
    public void undo() {

    }
}
