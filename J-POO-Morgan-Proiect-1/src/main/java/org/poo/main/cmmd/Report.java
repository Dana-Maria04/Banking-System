package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.ReportTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

public class Report extends Command {

    public Report(ArrayList<User> users, CommandInput command, ExchangeGraph exchangeGraph,
                  ArrayNode output, ObjectMapper objectMapper, ObjectNode commandNode,
                  ArrayList<Transaction> transactions) {
        super(users, commandNode, output, command, objectMapper, exchangeGraph, transactions, null);
    }

    @Override
    public void execute() {
        String targetIban = getCommand().getAccount();
        int startTimestamp = getCommand().getStartTimestamp();
        int endTimestamp = getCommand().getEndTimestamp();

        Account foundAccount = new Account();
        User associatedUser = new User();

        boolean accountFound = false;

        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(targetIban)) {
                    foundAccount = account;
                    associatedUser = user;
                    accountFound = true;
                    break;
                }
            }
            if (accountFound) {
                break;
            }
        }

        if (!accountFound) {
            foundAccount.addResponseToOutput(getObjectMapper(), getCommandNode(), getOutput(), getCommand(), "Account not found");
            return;
        }

        Map<String, Object> params = constructParams(getCommand().getDescription(), Map.of(
                "targetIban", targetIban,
                "startTimestamp", startTimestamp,
                "endTimestamp", endTimestamp,
                "account", foundAccount,
                "transactions", new ArrayList<>(getTransactions()),
                "user", associatedUser,
                "reportIban", foundAccount.getIban()
        ));

        ReportTransaction reportTransaction = (ReportTransaction) CreateTransaction.getInstance()
                .createTransaction("ReportTransaction", params);
        ObjectNode transactionNode = getObjectMapper().createObjectNode();
        reportTransaction.addDetailsToNode(transactionNode);
        getOutput().add(transactionNode);
    }

    @Override
    public void undo() {
        // Undo functionality not required
    }
}
