package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.Transaction;
import org.poo.main.userinfo.User;

import java.util.ArrayList;

public class SendMoney extends Command {
    public SendMoney(ArrayList<User> users, CommandInput command, ExchangeGraph exchangeGraph,
                     ArrayNode output, ObjectMapper objectMapper, ObjectNode commandNode) {
        super(users, commandNode, output, command, objectMapper, exchangeGraph);
    }

    @Override
    public void execute() {
        Account receiver = new Account();
        for (User user : getUsers()) {
            receiver = Account.searchAccount(getCommand().getReceiver(), user.getAccounts());
            if (receiver != null) {
                break;
            }
        }

        Account sender = new Account();
        User senderUser = new User();
        for (User user : getUsers()) {
            sender = Account.searchAccount(getCommand().getAccount(), user.getAccounts());
            if (sender != null) {
                senderUser = user;
                break;
            }
        }

        if(receiver == null || sender == null) {
            return;
        }

        if(sender.getBalance() < getCommand().getAmount()) {

            Transaction transaction = new Transaction();
            transaction.setDescription("Insufficient funds");
            transaction.setTimestamp(getCommand().getTimestamp());

            senderUser.getTransactions().add(transaction);
            return;
        }

        if(sender.getMinimumBalance() > sender.getBalance() - getCommand().getAmount()) {

            Transaction transaction = new Transaction();
            transaction.setDescription("Insufficient funds");
            transaction.setTimestamp(getCommand().getTimestamp());

            senderUser.getTransactions().add(transaction);

            return;
        }





        double amount = getGraph().convertCurrency(getCommand().getAmount(), sender.getCurrency(), receiver.getCurrency());


        Transaction transaction = new Transaction();
        transaction.setDescription(getCommand().getDescription());
        transaction.setTimestamp(getCommand().getTimestamp());
        transaction.setCurrency(sender.getCurrency());
        transaction.setAmount(getCommand().getAmount());
        transaction.setSenderIban(sender.getIban());
        transaction.setReceiverIban(receiver.getIban());
        transaction.setTransferType("sent");

        senderUser.getTransactions().add(transaction);

        receiver.incBalance(amount);
        sender.decBalance(getCommand().getAmount());
    }

    @Override
    public void undo() {

    }
}
