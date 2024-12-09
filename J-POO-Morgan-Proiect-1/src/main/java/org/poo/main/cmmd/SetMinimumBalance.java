package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class SetMinimumBalance extends Command {

    public SetMinimumBalance(ArrayList<User> users, CommandInput command) {
        super(users, null, null, command, null, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(getCommand().getAccount())) {
                    account.setMinimumBalance(getCommand().getMinBalance());
                    return;
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}
