package org.poo.main.userinfo.transactions;

import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateTransaction implements TransactionFactory {
    private static CreateTransaction instance;

    private CreateTransaction() {}

    public static CreateTransaction getInstance() {
        if (instance == null) {
            instance = new CreateTransaction();
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
            case "ChangeInterestRate":
                return new ChangeInterestRateTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("iban"),
                        ((Double) params.get("interestRate")).doubleValue()
                );
            case "CheckCardStatus":
                return new CheckCardStatusTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("cardNumber"),
                        (String) params.get("iban"),
                        (String) params.get("status")
                );
            case "CreateCard":
                return new CreateCardTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("iban"),
                        (String) params.get("cardNumber"),
                        (String) params.get("userEmail"),
                        (String) params.get("iban")
                );
            case "DeleteAccount":
                return new DeleteAccountTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("iban")
                );
            case "FrozePayOnline":
                return new FrozenPayOnlineTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("iban")
                );
            case "MoneyTransfer":
                return new MoneyTransferTransaction(
                        description,
                        timestamp,
                        email,
                        ((Double) params.get("amount")).doubleValue(),
                        (String) params.get("currency"),
                        (String) params.get("senderIban"),
                        (String) params.get("receiverIban"),
                        (String) params.get("status"),
                        (String) params.get("accountIban")
                );
            case "PayOnline":
                return new PayOnlineTransaction(
                        description,
                        timestamp,
                        email,
                        ((Double) params.get("amount")).doubleValue(),
                        (String) params.get("commerciant"),
                        (String) params.get("iban")
                );
            case "ReportTransaction":
                return new ReportTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("targetIban"),
                        ((Integer) params.get("startTimestamp")).intValue(),
                        ((Integer) params.get("endTimestamp")).intValue(),
                        (Account) params.get("account"),
                        (ArrayList<Transaction>) params.get("transactions"),
                        (User) params.get("user"),
                        (String) params.get("reportIban")
                );
            case "SpendingsReport":
                return new SpendingReportTransaction(
                        description,
                        timestamp,
                        email,
                        (String) params.get("targetIban"),
                        ((Integer) params.get("startTimestamp")).intValue(),
                        ((Integer) params.get("endTimestamp")).intValue(),
                        (Account) params.get("account"),
                        (ArrayList<Transaction>) params.get("transactions"),
                        (User) params.get("user"),
                        (String) params.get("reportIban"),
                        (ArrayList<PayOnlineTransaction>) params.get("payOnlineTransactions")
                );
            case "SplitPayment":
                return new SplitPaymentTransaction(
                        (String) params.get("description"),
                        (int) params.get("timestamp"),
                        (String) params.get("email"),
                        ((Double) params.get("amount")).doubleValue(),
                        (String) params.get("currency"),
                        (List<String>) params.get("involvedAccounts"),
                        (String) params.get("iban"),
                        (String) params.get("error")
                );

            default:
                throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
    }
}
