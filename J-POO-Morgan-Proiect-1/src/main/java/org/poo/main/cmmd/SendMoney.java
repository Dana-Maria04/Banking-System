package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.transactions.MoneyTransferTransaction;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendMoney extends Command {
    public SendMoney(ArrayList<User> users, CommandInput command, ExchangeGraph exchangeGraph,
                     ArrayNode output, ObjectMapper objectMapper, ObjectNode commandNode,
                     ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, exchangeGraph, transactions, null);
    }

    @Override
    public void execute() {
        Account receiver = new Account();
        User receiverUser = new User();
        for (User user : getUsers()) {
            receiver = Account.searchAccount(getCommand().getReceiver(), user.getAccounts());
            if (receiver != null) {
                receiverUser = user;
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

        if (receiver == null || sender == null) {
            return;
        }

        if (sender.getBalance() < getCommand().getAmount()) {
            Map<String, Object> params = new HashMap<>();
            params.put("description", "Insufficient funds");
            params.put("timestamp", getCommand().getTimestamp());
            params.put("email", senderUser.getUser().getEmail());
            params.put("amount", 0.0);
            params.put("currency", sender.getCurrency());
            params.put("senderIban", sender.getIban());
            params.put("receiverIban", receiver.getIban());
            params.put("status", "failed");
            params.put("accountIban", sender.getIban());

            Transaction transaction = CreateTransaction.getInstance().createTransaction("MoneyTransfer", params);


            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }

        if (sender.getMinimumBalance() > sender.getBalance() - getCommand().getAmount()) {
            Map<String, Object> params = new HashMap<>();
            params.put("description", "Insufficient funds (below minimum balance)");
            params.put("timestamp", getCommand().getTimestamp());
            params.put("email", senderUser.getUser().getEmail());
            params.put("amount", 0.0);
            params.put("currency", sender.getCurrency());
            params.put("senderIban", sender.getIban());
            params.put("receiverIban", receiver.getIban());
            params.put("status", "failed");
            params.put("accountIban", sender.getIban());

            Transaction transaction = CreateTransaction.getInstance().createTransaction("MoneyTransfer", params);


            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }

        double amount = getGraph().convertCurrency(getCommand().getAmount(), sender.getCurrency(), receiver.getCurrency());

        Map<String, Object> senderParams = new HashMap<>();
        senderParams.put("description", getCommand().getDescription());
        senderParams.put("timestamp", getCommand().getTimestamp());
        senderParams.put("email", senderUser.getUser().getEmail());
        senderParams.put("amount", getCommand().getAmount());
        senderParams.put("currency", sender.getCurrency());
        senderParams.put("senderIban", sender.getIban());
        senderParams.put("receiverIban", receiver.getIban());
        senderParams.put("status", "sent");
        senderParams.put("accountIban", sender.getIban());

        MoneyTransferTransaction senderTransaction = (MoneyTransferTransaction) CreateTransaction.getInstance().createTransaction("MoneyTransfer", senderParams);

        senderTransaction.setEmail(senderUser.getUser().getEmail());

        Map<String, Object> receiverParams = new HashMap<>();
        receiverParams.put("description", getCommand().getDescription());
        receiverParams.put("timestamp", getCommand().getTimestamp());
        receiverParams.put("email", receiverUser.getUser().getEmail());
        receiverParams.put("amount", amount);
        receiverParams.put("currency", receiver.getCurrency());
        receiverParams.put("senderIban", sender.getIban());
        receiverParams.put("receiverIban", receiver.getIban());
        receiverParams.put("status", "received");
        receiverParams.put("accountIban", receiver.getIban());

        MoneyTransferTransaction receiverTransaction = (MoneyTransferTransaction) CreateTransaction.getInstance().createTransaction("MoneyTransfer", receiverParams);

        receiverTransaction.setEmail(receiverUser.getUser().getEmail());

        getTransactions().add(senderTransaction);
        getTransactions().add(receiverTransaction);


        receiver.incBalance(amount);
        sender.decBalance(getCommand().getAmount());
    }

    @Override
    public void undo() {

    }
}