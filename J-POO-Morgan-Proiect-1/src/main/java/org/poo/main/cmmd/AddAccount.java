package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Transaction;
import org.poo.main.userinfo.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class AddAccount extends Command {

    public AddAccount(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper, null);
    }

    @Override
    public void execute() {

        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                Account newAccount = new Account(Utils.generateIBAN(), getCommand().getAccountType(), getCommand().getCurrency(), 0);
                newAccount.setCards(new ArrayList<>());

                ArrayList<Account> existingAccounts = user.getAccounts();
                existingAccounts.add(newAccount);
                user.setAccounts(existingAccounts);

                Transaction transaction = new Transaction();
                transaction.setDescription("New account created");
                transaction.setTimestamp(getCommand().getTimestamp());

                user.getTransactions().add(transaction);

                break;
            }
        }

    }

    @Override
    public void undo() {

    }
}
