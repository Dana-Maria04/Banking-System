package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;
import org.poo.utils.Utils;

public class CreateCard implements Command {
    private User[] users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;

    public CreateCard(User[] users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
    }




    @Override
    public User[] execute() {

        for(User user : users) {
            if(user.getUser().getEmail().equals(command.getEmail())) {
                for(Account account : user.getAccounts()) {

                    if(account.getIban().equals(command.getAccount())) {


                        Card newCard = new Card(Utils.generateCardNumber(), "active");
                        Card[] existingCards = account.getCards();
                        if (existingCards == null) {
                            existingCards = new Card[0];
                        }
                        Card[] updatedCards = new Card[existingCards.length + 1];
                        for(int i = 0; i < existingCards.length; i++) {
                            updatedCards[i] = existingCards[i];
                        }
                        updatedCards[existingCards.length] = newCard;
                        account.setCards(updatedCards);
                        break;



                    }
                }
            }
        }

        return this.users;
    }

    @Override
    public void undo() {

    }
}
