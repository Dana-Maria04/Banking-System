package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckCardStatus extends Command {

    public CheckCardStatus(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                           ObjectMapper objectMapper, ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(getCommand().getCardNumber())) {
                        if (account.getBalance() - account.getMinimumBalance() <= 30) {
                            Map<String, Object> params = new HashMap<>();
                            params.put("description", "You have reached the minimum amount of funds, the card will be frozen");
                            params.put("timestamp", getCommand().getTimestamp());
                            params.put("email", user.getUser().getEmail());
                            params.put("cardNumber", card.getCardNumber());
                            params.put("iban", account.getIban());
                            params.put("status", "frozen");

                            Transaction transaction = CreateTransaction.getInstance().createTransaction("CheckCardStatus", params);
                            getTransactions().add(transaction);

                            card.setFrozen(1);
                            card.setStatus("frozen");
                        }
                        return;
                    }
                }
            }
        }

        ObjectNode outputNode = getObjectMapper().createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", getCommand().getTimestamp());
        ObjectNode commandOutput = getObjectMapper().createObjectNode();
        commandOutput.set("output", outputNode);
        commandOutput.put("command", "checkCardStatus");
        commandOutput.put("timestamp", getCommand().getTimestamp());
        getOutput().add(commandOutput);
    }

    @Override
    public void undo() {

    }
}