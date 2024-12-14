package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.transactions.CheckCardStatusTransaction;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

public class CheckCardStatus extends Command {

    public CheckCardStatus(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                           ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, null);
    }

    @Override
    public void execute() {
        boolean cardFound = false;

        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(getCommand().getCardNumber())) {
                        cardFound = true;

                        if (account.getBalance() - account.getMinimumBalance() <= 30) {
                            CheckCardStatusTransaction transaction = new CheckCardStatusTransaction(
                                    "You have reached the minimum amount of funds, the card will be frozen",
                                    getCommand().getTimestamp(),
                                    user.getUser().getEmail(),
                                    card.getCardNumber(),
                                    account.getIban(),
                                    "frozen"
                            );

                            getTransactions().add(transaction);

                            card.setFrozen(1);
                            card.setStatus("frozen");
                        }
                        return;
                    }
                }
            }
        }

        if (!cardFound) {
            ObjectNode outputNode = getObjectMapper().createObjectNode();
            outputNode.put("description", "Card not found");
            outputNode.put("timestamp", getCommand().getTimestamp());
            ObjectNode commandOutput = getObjectMapper().createObjectNode();
            commandOutput.set("output", outputNode);
            commandOutput.put("command", "checkCardStatus");
            commandOutput.put("timestamp", getCommand().getTimestamp());
            getOutput().add(commandOutput);
        }
    }

    @Override
    public void undo() {

    }
}