package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class AddInterestRate extends Command{
    public AddInterestRate(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                      ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper, null, null, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(getCommand().getAccount())) {

                    if(account.getAccountType().equals("classic")) {
                        account.addResponseToOutput(getObjectMapper(), getCommandNode(), getOutput(), getCommand(), "This is not a savings account");
                        return;
                    }

                    account.setBalance(account.getBalance() + account.getBalance() * account.getInterestRate());

                    return;
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}
