package org.poo.main.cmmd;

import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Card;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateCard extends Command {
    private int oneTime;

    public CreateCard(ArrayList<User> users, CommandInput command, int oneTime, ArrayList<Transaction> transactions) {
        super(users, null, null, command, null, null, transactions, null);
        this.oneTime = oneTime;
    }

    @Override
    public void execute() {
        for (User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(getCommand().getAccount())) {

                        Card newCard = new Card(Utils.generateCardNumber(), "active", oneTime);
                        newCard.setFrozen(0);

                        Map<String, Object> params = new HashMap<>();
                        params.put("description", "New card created");
                        params.put("timestamp", getCommand().getTimestamp());
                        params.put("email", user.getUser().getEmail());
                        params.put("iban", account.getIban());
                        params.put("cardNumber", newCard.getCardNumber());
                        params.put("userEmail", user.getUser().getEmail());

                        Transaction transaction = CreateTransaction.getInstance().createTransaction("CreateCard", params);

                        getTransactions().add(transaction);

                        account.getCards().add(newCard);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void undo() {

    }
}