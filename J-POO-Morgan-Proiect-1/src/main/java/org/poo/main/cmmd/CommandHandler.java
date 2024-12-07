package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;
import java.util.Arrays;

// invoker
public class CommandHandler {

    private User[] users;
    private ArrayList<CommandInput> commands;


    public ArrayNode execute(final ObjectInput objectInput, ArrayNode output) {


        users = new User[objectInput.getUsers().length];
        for (int i = 0; i < objectInput.getUsers().length; i++) {
            UserInput userInput = objectInput.getUsers()[i];
            User user = new User(new Account[0]);
            user.setUser(userInput);
//            user.setAccounts(new Account[0]);
            users[i] = user;

        }
        // set the commands from the input
        commands = new ArrayList<>(Arrays.asList(objectInput.getCommands()));

        ObjectMapper objectMapper = new ObjectMapper();

        for(CommandInput cmd : commands) {

            ObjectNode commandNode = objectMapper.createObjectNode();


            switch(cmd.getCommand()) {
                case "printUsers":
                    PrintUsers printUsers = new PrintUsers(users, commandNode, output, cmd, objectMapper);
                    printUsers.execute();
                    break;
                case "addFunds":
                    AddFunds addFunds = new AddFunds(users, commandNode, output, cmd, objectMapper);
                    addFunds.execute();
                    break;
                case "createCard":
                    CreateCard createCard = new CreateCard(users, commandNode, output, cmd, objectMapper);
                    createCard.execute();
                    break;
                case "addAccount":
                    AddAccount addAccount = new AddAccount(users, commandNode, output, cmd, objectMapper);
                    addAccount.execute();

                    break;
                default:
                    break;

            }
        }

        return output;
    }

}
