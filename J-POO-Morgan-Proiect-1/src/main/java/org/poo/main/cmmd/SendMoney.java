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

import java.util.ArrayList;

public class SendMoney extends Command {
    public SendMoney(ArrayList<User> users, CommandInput command, ExchangeGraph exchangeGraph,
                     ArrayNode output, ObjectMapper objectMapper, ObjectNode commandNode,
                     ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, exchangeGraph, transactions, null);
    }

    @Override
    public void execute() {
        Account receiver = null;
        User receiverUser = null;
        for (User user : getUsers()) {
            receiver = Account.searchAccount(getCommand().getReceiver(), user.getAccounts());
            if (receiver != null) {
                receiverUser = user;
                break;
            }
        }

        Account sender = null;
        User senderUser = null;
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
            MoneyTransferTransaction transaction = new MoneyTransferTransaction(
                    "Insufficient funds",
                    getCommand().getTimestamp(),
                    senderUser.getUser().getEmail(),
                    0,
                    sender.getCurrency(),
                    sender.getIban(),
                    receiver.getIban(),
                    "failed",
                    sender.getIban()
            );

            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }

        if (sender.getMinimumBalance() > sender.getBalance() - getCommand().getAmount()) {
            MoneyTransferTransaction transaction = new MoneyTransferTransaction(
                    "Insufficient funds",
                    getCommand().getTimestamp(),
                    senderUser.getUser().getEmail(),
                    0,
                    sender.getCurrency(),
                    sender.getIban(),
                    receiver.getIban(),
                    "failed",
                    sender.getIban()
            );

            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }

        double amount = getGraph().convertCurrency(getCommand().getAmount(), sender.getCurrency(), receiver.getCurrency());

        MoneyTransferTransaction senderTransaction = new MoneyTransferTransaction(
                getCommand().getDescription(),
                getCommand().getTimestamp(),
                senderUser.getUser().getEmail(),
                getCommand().getAmount(),
                sender.getCurrency(),
                sender.getIban(),
                receiver.getIban(),
                "sent",
                sender.getIban()
        );
        senderTransaction.setEmail(senderUser.getUser().getEmail());

        MoneyTransferTransaction receiverTransaction = new MoneyTransferTransaction(
                getCommand().getDescription(),
                getCommand().getTimestamp(),
                receiverUser.getUser().getEmail(),
                amount,
                receiver.getCurrency(),
                sender.getIban(),
                receiver.getIban(),
                "received",
                receiver.getIban()
        );
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