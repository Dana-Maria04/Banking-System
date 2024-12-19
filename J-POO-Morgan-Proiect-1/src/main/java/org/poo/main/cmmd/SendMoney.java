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

            Map<String, Object> insufficientFundsParams = constructParams(
                    "Insufficient funds",
                    Map.of(
                        "amount", 0.0,
                        "currency", sender.getCurrency(),
                        "senderIban", sender.getIban(),
                        "receiverIban", receiver.getIban(),
                        "status", "failed",
                        "accountIban", sender.getIban()
                    )
            );

            Transaction transaction = CreateTransaction.getInstance().createTransaction("MoneyTransfer", insufficientFundsParams);


            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }

        if (sender.getMinimumBalance() > sender.getBalance() - getCommand().getAmount()) {
            Map<String, Object> belowMinimumBalanceParams = constructParams(
                    "Insufficient funds (below minimum balance)",
                    Map.of(
                        "amount", 0.0,
                        "currency", sender.getCurrency(),
                        "senderIban", sender.getIban(),
                        "receiverIban", receiver.getIban(),
                        "status", "failed",
                        "accountIban", sender.getIban()
                    )
            );

            Transaction transaction = CreateTransaction.getInstance().createTransaction("MoneyTransfer", belowMinimumBalanceParams);


            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }

        double amount = getGraph().convertCurrency(getCommand().getAmount(), sender.getCurrency(), receiver.getCurrency());

        Map<String, Object> senderParams = constructParams(
                getCommand().getDescription(),
                Map.of(
                    "amount", getCommand().getAmount(),
                    "currency", sender.getCurrency(),
                    "senderIban", sender.getIban(),
                    "receiverIban", receiver.getIban(),
                    "status", "sent",
                    "accountIban", sender.getIban()
                )
        );

        MoneyTransferTransaction senderTransaction = (MoneyTransferTransaction) CreateTransaction.getInstance()
                .createTransaction("MoneyTransfer", senderParams);

        senderTransaction.setEmail(senderUser.getUser().getEmail());

        Map<String, Object> receiverParams = constructParams(
                getCommand().getDescription(),
                Map.of(
                    "amount", amount,
                    "currency", receiver.getCurrency(),
                    "senderIban", sender.getIban(),
                    "receiverIban", receiver.getIban(),
                    "status", "received",
                    "accountIban", receiver.getIban()
                )
        );

        MoneyTransferTransaction receiverTransaction = (MoneyTransferTransaction) CreateTransaction.getInstance()
                .createTransaction("MoneyTransfer", receiverParams);

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