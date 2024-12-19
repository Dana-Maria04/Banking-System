package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

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

                    if (account.getAccountType().equals("classic")) {
                        account.addResponseToOutput(
                                getObjectMapper(),
                                getCommandNode(),
                                getOutput(),
                                getCommand(),
                                "This is not a savings account"
                        );
                        return;
                    }

                    account.setInterestRate(getCommand().getInterestRate());
                    Map<String, Object> params = Map.of(
                            "description", "Interest rate of the account changed to " + getCommand().getInterestRate(),
                            "timestamp", getCommand().getTimestamp(),
                            "email", user.getUser().getEmail(),
                            "iban", account.getIban(),
                            "interestRate", getCommand().getInterestRate()
                    );

                    // Use TransactionFactory to create the transaction
                    Transaction transaction = CreateTransaction.getInstance().createTransaction("ChangeInterestRate", params);

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
