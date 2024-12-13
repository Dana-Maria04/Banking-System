package org.poo.main.userinfo.transactions;

import org.poo.main.userinfo.transactions.AddAccountTransaction;
import org.poo.main.userinfo.transactions.CardDeletionTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.Map;

public class TransactionFactory implements ITransactionFactory {
    private static TransactionFactory instance;

    private TransactionFactory() {}

    public static TransactionFactory getInstance() {
        if (instance == null) {
            instance = new TransactionFactory();
        }
        return instance;
    }

    @Override
    public Transaction createTransaction(String type, Map<String, Object> params) {
        String description = (String) params.get("description");
        int timestamp = ((Integer) params.get("timestamp")).intValue();
        String email = (String) params.get("email");

        switch (type) {
            case "AddAccount":
                return new AddAccountTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("accountType"),
                        (String) params.get("currency"),
                        (String) params.get("iban")
                );
            case "DeleteCard":
                return new CardDeletionTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("iban"),
                        (String) params.get("cardNumber")
                );
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
    }
}
