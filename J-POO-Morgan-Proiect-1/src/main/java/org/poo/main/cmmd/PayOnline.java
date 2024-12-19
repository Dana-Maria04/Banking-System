package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

public class PayOnline extends Command {

    public PayOnline(ArrayList<User> users, CommandInput command, ExchangeGraph exchangeGraph,
                     ArrayNode output, ObjectMapper objectMapper, ObjectNode commandNode,
                     ArrayList<Transaction> transactions,
                     ArrayList<PayOnlineTransaction> payOnlineTransactions) {
        super(users, commandNode, output, command, objectMapper,
                exchangeGraph, transactions, payOnlineTransactions);
    }

    @Override
    public void execute() {

        ExchangeGraph exchangeGraph = getGraph();

        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (Account account : user.getAccounts()) {

                    double convertedAmount = exchangeGraph.convertCurrency(
                            getCommand().getAmount(),
                            getCommand().getCurrency(),
                            account.getCurrency()
                    );
                    account.pay(convertedAmount, getCommand().getCardNumber(), account.getCards(),
                            user, getCommand(), getTransactions(), account.getIban(),
                            getSpendingsReportTransactions(), account);
                    if(account.getFoundCard() == 1 || account.getInsufficientFunds() == 1) {
                        return;
                    }
                }
            }
        }
        Account tempAccount = new Account();
        tempAccount.addResponseToOutput(
                getObjectMapper(),
                getCommandNode(),
                getOutput(),
                getCommand(),
                "Card not found"
        );
    }

    @Override
    public void undo() {

    }
}