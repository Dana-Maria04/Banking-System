package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class DeleteCard extends Command {

    public DeleteCard(ArrayList<User> users, CommandInput command) {
        super(users, null, null, command, null, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (Account account : user.getAccounts()) {
                    account.getCards().removeIf(card -> card.getCardNumber().equals(getCommand().getCardNumber()));
                }
                return;
            }
        }
    }

    @Override
    public void undo() {

    }
}
