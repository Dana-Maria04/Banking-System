package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.ReportTransaction;
import org.poo.main.userinfo.transactions.SpendingReportTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

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

                    SpendingReportTransaction SpendingReportTransaction = new SpendingReportTransaction(
                            getCommand().getDescription(),
                            getCommand().getTimestamp(),
                            user.getUser().getEmail(),
                            targetIban,
                            startTimestamp,
                            endTimestamp,
                            account,
                            getTransactions(),
                            user,
                            account.getIban(),
                            getSpendingsReportTransactions()
                    );

                    ObjectNode transactionNode = getObjectMapper().createObjectNode();
                    SpendingReportTransaction.addDetailsToNode(transactionNode);
                    getOutput().add(transactionNode);
                    return;
                }
            }
        }

        ObjectNode errorNode = getObjectMapper().createObjectNode();
        errorNode.put("error", "No account found");
        getOutput().add(errorNode);
    }

    @Override
    public void undo() {

    }
}