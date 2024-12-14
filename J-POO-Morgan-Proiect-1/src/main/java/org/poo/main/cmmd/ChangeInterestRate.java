package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.ChangeInterestRateTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

public class ChangeInterestRate extends Command {
    public ChangeInterestRate(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                              ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(getCommand().getAccount())) {

                    if(account.getAccountType().equals("classic")) {
                        ObjectNode outputNode = getObjectMapper().createObjectNode();
                        getCommandNode().put("command", getCommand().getCommand());
                        outputNode.put("description", "This is not a savings account");
                        outputNode.put("timestamp", getCommand().getTimestamp());
                        getCommandNode().put("timestamp", getCommand().getTimestamp());
                        getCommandNode().set("output", outputNode);
                        getOutput().add(getCommandNode());
                        return;
                    }

                    account.setInterestRate(getCommand().getInterestRate());

                    ChangeInterestRateTransaction transaction = new ChangeInterestRateTransaction(
                            "Interest rate of the account changed to " + getCommand().getInterestRate(),
                            getCommand().getTimestamp(),
                            user.getUser().getEmail(),
                            account.getIban(),
                            getCommand().getInterestRate()
                    );
                    getTransactions().add(transaction);
                    return;
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}
