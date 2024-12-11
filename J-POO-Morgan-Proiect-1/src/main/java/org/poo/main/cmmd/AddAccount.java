package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.transactions.AddAccountTransaction;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class AddAccount extends Command {

    public AddAccount(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {


                Account newAccount = new Account(Utils.generateIBAN(), getCommand().getAccountType(), getCommand().getCurrency(), 0);
                newAccount.setCards(new ArrayList<>());

                user.getAccounts().add(newAccount);

                AddAccountTransaction transaction = new AddAccountTransaction(
                        "New account created",
                        getCommand().getTimestamp(),
                        user.getUser().getEmail(),
                        getCommand().getAccountType(),
                        getCommand().getCurrency(),
                        newAccount.getIban()
                );

                getTransactions().add(transaction);

                break;
            }
        }
    }

    @Override
    public void undo() {

    }
}
