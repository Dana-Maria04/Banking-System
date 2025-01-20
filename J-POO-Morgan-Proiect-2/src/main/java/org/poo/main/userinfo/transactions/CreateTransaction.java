package org.poo.main.userinfo.transactions;

import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.splitTransactions.InsufficientFundsSplitCustomTransaction;
import org.poo.main.userinfo.transactions.splitTransactions.InsufficientFundsSplitEqualTransaction;
import org.poo.main.userinfo.transactions.splitTransactions.SplitPaymentCustomGoodTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A factory class to create different types of transactions.
 */
public final class CreateTransaction implements TransactionFactory {

    private static CreateTransaction instance;

    private CreateTransaction() {

    }

    /**
     * Returns the singleton instance of the CreateTransaction class.
     *
     * @return The singleton instance of CreateTransaction.
     */
    public static CreateTransaction getInstance() {
        if (instance == null) {
            instance = new CreateTransaction();
        }
        return instance;
    }

    /**
     * Creates a transaction based on the provided type and parameters.
     *
     * @param type  The type of the transaction to create.
     * @param params The parameters required for the transaction.
     * @return A new transaction object based on the provided type and parameters.
     * @throws IllegalArgumentException if the transaction type is unknown.
     */
    @Override
    public Transaction createTransaction(final String type, final Map<String, Object> params) {
        final String description = (String) params.get("description");
        final int timestamp = (Integer) params.get("timestamp");
        final String email = (String) params.get("email");

        return switch (type) {
            case "AddAccount" -> new AddAccountTransaction(
                    description, timestamp, email,
                    (String) params.get("accountType"),
                    (String) params.get("currency"),
                    (String) params.get("iban")
            );
            case "DeleteCard" -> new CardDeletionTransaction(
                    description, timestamp, email,
                    (String) params.get("iban"),
                    (String) params.get("cardNumber")
            );
            case "ChangeInterestRate" -> new ChangeInterestRateTransaction(
                    description, timestamp, email,
                    (String) params.get("iban")
            );
            case "CheckCardStatus" -> new CheckCardStatusTransaction(
                    description, timestamp, email,
                    (String) params.get("iban")
            );
            case "CreateCard" -> new CreateCardTransaction(
                    description, timestamp, email,
                    (String) params.get("iban"),
                    (String) params.get("cardNumber"),
                    (String) params.get("userEmail"),
                    (String) params.get("iban")
            );
            case "DeleteAccount" -> new DeleteAccountTransaction(
                    description, timestamp, email,
                    (String) params.get("iban")
            );
            case "FrozePayOnline" -> new FrozenPayOnlineTransaction(
                    description, timestamp, email,
                    (String) params.get("iban")
            );
            case "MoneyTransfer" -> new MoneyTransferTransaction(
                    description, timestamp, email,
                    (Double) params.get("amount"),
                    (String) params.get("currency"),
                    (String) params.get("senderIban"),
                    (String) params.get("receiverIban"),
                    (String) params.get("status"),
                    (String) params.get("accountIban")
            );
            case "PayOnline" -> new PayOnlineTransaction(
                    description, timestamp, email,
                    (Double) params.get("amount"),
                    (String) params.get("commerciant"),
                    (String) params.get("iban")
            );
            case "ReportTransaction" -> new ReportTransaction(
                    description, timestamp, email,
                    (String) params.get("targetIban"),
                    (Integer) params.get("startTimestamp"),
                    (Integer) params.get("endTimestamp"),
                    (Account) params.get("account"),
                    (ArrayList<Transaction>) params.get("transactions"),
                    (User) params.get("user"),
                    (String) params.get("reportIban")
            );
            case "SpendingsReport" -> new SpendingReportTransaction(
                    description, timestamp, email,
                    (String) params.get("targetIban"),
                    (Integer) params.get("startTimestamp"),
                    (Integer) params.get("endTimestamp"),
                    (Account) params.get("account"),
                    (String) params.get("reportIban"),
                    (ArrayList<PayOnlineTransaction>) params.get("payOnlineTransactions")
            );
            case "SplitPayment" -> new SplitPaymentTransaction(
                    (String) params.get("description"),
                    (int) params.get("timestamp"),
                    (String) params.get("email"),
                    (Double) params.get("amount"),
                    (String) params.get("currency"),
                    (List<String>) params.get("involvedAccounts"),
                    (String) params.get("iban"),
                    (String) params.get("error")
            );
            case "InsufficientFundsSplit" -> new InsufficientFundsSplitCustomTransaction.Builder()
                    .setDescription((String) params.get("description"))
                    .setTimestamp((int) params.get("timestamp"))
                    .setEmail((String) params.get("email"))
                    .setTotalAmount((double) params.get("totalAmount"))
                    .setCurrency((String) params.get("currency"))
                    .setAmountForUsers((List<Double>) params.get("amountForUsers"))
                    .setInvolvedAccounts((List<String>) params.get("involvedAccounts"))
                    .setSplitPaymentType((String) params.get("splitPaymentType"))
                    .setError((String) params.get("error"))
                    .build();

            case "InsufficientFundsSplitEqualTransaction" -> new InsufficientFundsSplitEqualTransaction.Builder()
                    .setDescription((String) params.get("description"))
                    .setTimestamp((int) params.get("timestamp"))
                    .setEmail((String) params.get("email"))
                    .setAmountsForUsers((List<Double>) params.get("amountsForUsers"))
                    .setCurrency((String) params.get("currency"))
                    .setTotalAmount((double) params.get("totalAmount"))
                    .setInvolvedAccounts((List<String>) params.get("involvedAccounts"))
                    .setSplitPaymentType((String) params.get("splitPaymentType"))
                    .setError((String) params.get("error"))
                    .build();

            case "SplitPaymentCustomGoodTransaction" -> new SplitPaymentCustomGoodTransaction.Builder()
                    .setDescription((String) params.get("description"))
                    .setTimestamp((int) params.get("timestamp"))
                    .setEmail((String) params.get("email"))
                    .setAmountsForUsers((List<Double>) params.get("amountsForUsers"))
                    .setCurrency((String) params.get("currency"))
                    .setInvolvedAccounts((List<String>) params.get("involvedAccounts"))
                    .setSplitPaymentType((String) params.get("splitPaymentType"))
                    .build();


            case "WithdrawSavings" -> new WithdrawSavingsTransaction(
                    (String) params.get("description"),
                    (int) params.get("timestamp"),
                    (String) params.get("email")
            );
            case "UpgradePlan" -> new UpgradePlanTransaction(
                    description,
                    timestamp,
                    email,
                    (String) params.get("iban"),
                    (String) params.get("newPlanType")
            );
            case "cashWithdrawal" -> new CashWithdrawalTransaction(
                    description,
                    timestamp,
                    email,
                    (String) params.get("iban"),
                    (Double) params.get("amount"),
                    (String) params.get("currency")
            );
            case "InterestTransaction" -> new InterestTransaction(
                    (Double) params.get("income"),
                    (String) params.get("currency"),
                    description,
                    timestamp,
                    email
            );
            default -> throw new IllegalArgumentException("Unknown transaction type: " + type);
        };
    }
}
