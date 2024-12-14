package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

public class DeleteAccount extends Command {

    public DeleteAccount(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                ArrayList<Account> accounts = user.getAccounts();

                Account targetAccount = null;
                for (Account acc : accounts) {
                    if (acc.getIban().equals(getCommand().getAccount())) {
                        targetAccount = acc;
                        break;
                    }
                }

                if (targetAccount.getBalance() == 0) {
                    accounts.remove(targetAccount);
                    addResponseToOutput("success", "Account deleted");
                } else {
                    addResponseToOutput("error", "Account couldn't be deleted - see org.poo.transactions for details");
                }

                return;
            }
        }

        addResponseToOutput("error", "User not found");
    }

    @Override
    public void undo() {

    }
}