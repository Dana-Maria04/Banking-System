package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.Transaction;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class PrintTransactions extends Command{
    public PrintTransactions(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command, ObjectMapper objectMapper) {
        super(users, commandNode, output, command, objectMapper, null);
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                getCommandNode().put("command", getCommand().getCommand());
                getCommandNode().put("timestamp", getCommand().getTimestamp());

                ArrayNode transactionsArray = getObjectMapper().createArrayNode();

                for (Transaction transaction : user.getTransactions()) {
                    ObjectNode transactionNode = getObjectMapper().createObjectNode();
                    transactionNode.put("timestamp", transaction.getTimestamp());
                    transactionNode.put("description", transaction.getDescription());

                    if (transaction.getSenderIban() != null) {
                        transactionNode.put("senderIBAN", transaction.getSenderIban());
                    }

                    if (transaction.getReceiverIban() != null) {
                        transactionNode.put("receiverIBAN", transaction.getReceiverIban());
                    }

                    if (transaction.getAmount() != 0) {
                        transactionNode.put("amount", transaction.getAmount() + " " + transaction.getCurrency());
                    }

                    if (transaction.getTransferType() != null) {
                        transactionNode.put("transferType", transaction.getTransferType());
                    }

                    if(transaction.getCardNumber() != null) {
                        transactionNode.put("card", transaction.getCardNumber());
                    }

                    if(transaction.getAccount() != null) {
                        transactionNode.put("account", transaction.getAccount());
                    }

                    if(transaction.getCardHolder() != null) {
                        transactionNode.put("cardHolder", transaction.getCardHolder());
                    }

                    if(transaction.getAmountPayOnline() > 0) {
                        transactionNode.put("amount", transaction.getAmountPayOnline());
                    }

                    if(transaction.getCommerciant() != null) {
                        transactionNode.put("commerciant", transaction.getCommerciant());
                    }

                    transactionsArray.add(transactionNode);
                }

                getCommandNode().set("output", transactionsArray);

                getOutput().add(getCommandNode());

                break;
            }
        }
    }


    @Override
    public void undo() {

    }
}
