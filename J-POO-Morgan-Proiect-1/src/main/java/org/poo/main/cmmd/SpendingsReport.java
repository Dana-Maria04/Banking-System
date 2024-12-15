package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpendingsReport extends Command {

    public SpendingsReport(ArrayList<User> users, ObjectNode commandNode, ArrayNode output, CommandInput command,
                           ObjectMapper objectMapper, ArrayList<Transaction> transactions,
                           ArrayList<PayOnlineTransaction> payOnlineTransactions) {
        super(users, commandNode, output, command, objectMapper, null, transactions, payOnlineTransactions);
    }


    @Override
    public void execute() {
        String targetIban = getCommand().getAccount();
        int startTimestamp = getCommand().getStartTimestamp();
        int endTimestamp = getCommand().getEndTimestamp();

        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(targetIban)) {

                    if(account.getAccountType().equals("savings")) {
                        ObjectNode errorNode = getObjectMapper().createObjectNode();
                        errorNode.put("command", getCommand().getCommand());
                        ObjectNode outputNode = errorNode.putObject("output");
                        outputNode.put("error", "This kind of report is not supported for a saving account");
                        errorNode.put("timestamp", getCommand().getTimestamp());
                        getOutput().add(errorNode);
                        return ;
                    }

                    Map<String, Object> params = new HashMap<>();
                    params.put("description", getCommand().getDescription());
                    params.put("timestamp", getCommand().getTimestamp());
                    params.put("email", user.getUser().getEmail());
                    params.put("targetIban", targetIban);
                    params.put("startTimestamp", startTimestamp);
                    params.put("endTimestamp", endTimestamp);
                    params.put("account", account);
                    params.put("transactions", getTransactions());
                    params.put("user", user);
                    params.put("reportIban", account.getIban());
                    params.put("payOnlineTransactions", getSpendingsReportTransactions());

                    SpendingReportTransaction spendingTransaction = (SpendingReportTransaction)
                            CreateTransaction.getInstance().createTransaction("SpendingsReport", params);

                    ObjectNode transactionNode = getObjectMapper().createObjectNode();
                    spendingTransaction.addDetailsToNode(transactionNode);
                    getOutput().add(transactionNode);
                    return;
                }
            }
        }
        ObjectNode errorNode = getObjectMapper().createObjectNode();
        errorNode.put("command", "spendingsReport");
        ObjectNode outputNode = errorNode.putObject("output");
        outputNode.put("description", "Account not found");
        outputNode.put("timestamp", getCommand().getTimestamp());
        errorNode.put("timestamp", getCommand().getTimestamp());
        getOutput().add(errorNode);
    }

    @Override
    public void undo() {

    }
}