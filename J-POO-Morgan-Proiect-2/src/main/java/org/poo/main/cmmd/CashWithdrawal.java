package org.poo.main.cmmd;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Map;

public class CashWithdrawal extends Command {

    public CashWithdrawal(final ArrayList<User> users, final ObjectNode commandNode,
                          final ArrayNode output, final CommandInput command,
                          final ObjectMapper objectMapper, final ArrayList<Transaction> transactions,
                          final ExchangeGraph graph) {
        super(users, commandNode, output, command, objectMapper, graph,
                transactions, null, null, null);
    }


    @Override
    public void execute() {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getAccountCards()) {
                    if (card.getCardNumber().equals(getCommand().getCardNumber())) {
                        double amount = getCommand().getAmount();
                        if (account.getBalance() - account.getMinimumBalance() < amount) {
                            Map<String, Object> params = Map.of(
                                    "description", "Insufficient funds",
                                    "timestamp", getCommand().getTimestamp(),
                                    "email", user.getUser().getEmail()
                            );
                            Transaction transaction = CreateTransaction.getInstance()
                                    .createTransaction("cashWithdrawal", params);
                            getTransactions().add(transaction);


                            return;
                        }

                        double cashback = 0;


                        if (user.getUserPlan().equals("standard")) {
                            double comision = amount * 0.002;
                            comision = getGraph().convertCurrency(comision, "RON", account.getCurrency());
                            account.setBalance(account.getBalance() - comision);
                        }

                        if (user.getUserPlan().equals("silver") && amount >= 500) {
                            double comision = amount * 0.001;
                            comision = getGraph().convertCurrency(comision,"RON", account.getCurrency());
                            account.setBalance(account.getBalance() - comision);
                        }

                        double righAmount = getGraph().convertCurrency(amount, "RON", account.getCurrency());
                        account.setBalance(account.getBalance() - righAmount + cashback);

                        Map<String, Object> params = Map.of(
                                "description", "Cash withdrawal of " + getCommand().getAmount(),
                                "timestamp", getCommand().getTimestamp(),
                                "email", user.getUser().getEmail(),
                                "iban", account.getAccountIban(),
                                "amount", getCommand().getAmount(),
                                "currency", account.getCurrency()
                        );

                        Transaction transaction = CreateTransaction.getInstance()
                                .createTransaction("cashWithdrawal", params);
                        getTransactions().add(transaction);
                        return;
                    }
                }
            }
        }

        ObjectNode errorNode = getObjectMapper().createObjectNode();
        errorNode.put("timestamp", getCommand().getTimestamp());
        errorNode.put("description", "Card not found");
        getCommandNode().set("output", errorNode);
        getCommandNode().put("command", "cashWithdrawal");
        getCommandNode().put("timestamp", getCommand().getTimestamp());
        getOutput().add(getCommandNode());
    }

    @Override
    public void undo() {

    }
}
