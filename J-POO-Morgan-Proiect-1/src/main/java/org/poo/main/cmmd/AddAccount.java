package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAccount extends Command {

    public AddAccount(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                      ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {


                Account newAccount = new Account(Utils.generateIBAN(), getCommand().getAccountType(),
                        getCommand().getCurrency(), 0, new ArrayList<>());
                newAccount.setCards(new ArrayList<>());
                newAccount.setInterestRate(getCommand().getInterestRate());

                user.getAccounts().add(newAccount);

                Map<String, Object> params = new HashMap<>();
                params.put("description", "New account created");
                params.put("timestamp", getCommand().getTimestamp());
                params.put("email", user.getUser().getEmail());
                params.put("accountType", getCommand().getAccountType());
                params.put("currency", getCommand().getCurrency());
                params.put("iban", newAccount.getIban());

                Transaction transaction = CreateTransaction.getInstance().createTransaction("AddAccount", params);
                getTransactions().add(transaction);

                break;
            }
        }
    }

    @Override
    public void undo() {

    }
}