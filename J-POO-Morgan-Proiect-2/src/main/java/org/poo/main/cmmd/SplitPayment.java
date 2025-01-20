package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.QueueSplitPayment;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.*;
import org.poo.main.userinfo.transactions.splitTransactions.InsufficientFundsSplitCustomTransaction;
import org.poo.main.userinfo.transactions.splitTransactions.InsufficientFundsSplitEqualTransaction;
import org.poo.main.userinfo.transactions.splitTransactions.SplitPaymentCustomGoodTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SplitPayment class is responsible for processing a split payment across multiple accounts,
 * taking into account conversion rates, balances, and minimum balance constraints.
 */
public class SplitPayment extends Command {

    /**
     * Constructs a SplitPayment command with the provided parameters.
     *
     * @param graph                     The ExchangeGraph for currency conversion.
     * @param users                     The list of users involved in the split payment.
     * @param commandNode               The command node containing the command details.
     * @param output                    The output node where the response will be written.
     * @param command                   The command input containing parameters for split payment.
     * @param objectMapper              ObjectMapper for JSON operations
     * @param transactions              The list of transactions associated with the users.
     */
    public SplitPayment(final ExchangeGraph graph, final ArrayList<User> users,
                        final ObjectNode commandNode, final ArrayNode output,
                        final CommandInput command, final ObjectMapper objectMapper,
                        final ArrayList<Transaction> transactions,
                        final ArrayList<QueueSplitPayment> queueSplitPayments) {
        super(users, commandNode, output, command, objectMapper, graph,
                transactions, null, null, queueSplitPayments);
    }

    /**
     * Executes the split payment command. It splits the amount across multiple accounts
     * and checks if any account has insufficient funds to complete the transaction.
     */
    @Override
    public void execute() {
        List<String> accounts = getCommand().getAccounts();
        double amountPerAccount = 0;
        if (accounts != null) {
            int numberOfAccounts = accounts.size();
            amountPerAccount = getCommand().getAmount() / numberOfAccounts;
        }

        if ("acceptSplitPayment".equals(getCommand().getCommand())) {
            System.out.printf("intru in accept\n");
            handleAcceptSplitPayment();
            return;
        }

        if ("splitPayment".equals(getCommand().getCommand())) {
            handleNewSplitPayment(accounts, amountPerAccount);
        }
    }

    private void handleAcceptSplitPayment() {
        String email = getCommand().getEmail();
        String splitPaymentType = getCommand().getSplitPaymentType();

        for (QueueSplitPayment pendingPayment : getQueueSplitPayments()) {
            if (splitPaymentType.equals(pendingPayment.getSplitPaymentType()) && !pendingPayment.isCompleted()) {
                for (String iban : pendingPayment.getAccounts()) {
                    User user = findUserByIBAN(iban);

                    if (user != null && user.getUser().getEmail().equals(email)) {
                        pendingPayment.accept(iban);

                        if (pendingPayment.isFullyAccepted()) {
                            boolean allAccountsValid = true;
                            String errorIban = null;

                            for (int i = 0; i < pendingPayment.getAccounts().size(); i++) {
                                String involvedIban = pendingPayment.getAccounts().get(i);
                                Account account = findAccountByIBAN(involvedIban);

                                if (account == null) {
                                    allAccountsValid = false;
                                    errorIban = involvedIban;
                                    break;
                                }

                                double requiredAmount;
                                if ("equal".equals(splitPaymentType)) {
                                    requiredAmount = getGraph().convertCurrency(
                                            pendingPayment.getTotalAmount() / pendingPayment.getAccounts().size(),
                                            pendingPayment.getCurrency(),
                                            account.getCurrency()
                                    );
                                } else { // "custom"

                                    System.out.printf("calculez la custom\n");

                                    requiredAmount = getGraph().convertCurrency(
                                            pendingPayment.getAmounts().get(i),
                                            pendingPayment.getCurrency(),
                                            account.getCurrency()
                                    );
                                }

                                if (account.getBalance() - account.getMinimumBalance() < requiredAmount) {
                                    allAccountsValid = false;
                                    errorIban = involvedIban;
                                    break;
                                }
                            }

                            if (!allAccountsValid) {

                                System.out.printf("toate conturile sunt valide\n");

                                for (int i = 0; i < pendingPayment.getAccounts().size(); i++) {
                                    String involvedIban = pendingPayment.getAccounts().get(i);
                                    User involvedUser = findUserByIBAN(involvedIban);

                                    System.out.printf("sunt si in for dupa toateconturile sunt valide\n");


                                    if (involvedUser != null) {
                                        double amountPerUser = 0.0;
                                        if(splitPaymentType.equals("equal")) {
                                            amountPerUser = pendingPayment.getTotalAmount() / pendingPayment.getAccounts().size();
                                        }

                                        System.out.printf("ajung sa decid la tranzactii\n");

//                                        Transaction transaction;
                                        if (splitPaymentType.equals("custom")) {

                                            System.out.printf("intru in primu if\n");

                                            Map<String, Object> params = Map.of(
                                                    "description", "Split payment of " + pendingPayment.getTotalAmount() + " " + pendingPayment.getCurrency(),
                                                    "timestamp", pendingPayment.getTimestamp(),
                                                    "email", involvedUser.getUser().getEmail(),
                                                    "totalAmount", pendingPayment.getTotalAmount(),
                                                    "currency", pendingPayment.getCurrency(),
                                                    "amountForUsers", pendingPayment.getAmounts(),
                                                    "involvedAccounts", pendingPayment.getAccounts(),
                                                    "splitPaymentType", splitPaymentType,
                                                    "error", "Account " + errorIban + " has insufficient funds for a split payment."
                                            );

                                            InsufficientFundsSplitCustomTransaction transaction = (InsufficientFundsSplitCustomTransaction)
                                                    CreateTransaction.getInstance().createTransaction("InsufficientFundsSplit", params);

                                            getTransactions().add(transaction);

                                        } else {

                                            System.out.printf("intru in al doilea if\n");

                                            Map<String, Object> params = Map.of(
                                                    "description", "Split payment of " + pendingPayment.getTotalAmount() + " " + pendingPayment.getCurrency(),
                                                    "timestamp", pendingPayment.getTimestamp(),
                                                    "email", involvedUser.getUser().getEmail(),
                                                    "amountsForUsers", pendingPayment.getAmounts(), // Lista de sume pentru utilizatori
                                                    "currency", pendingPayment.getCurrency(),
                                                    "totalAmount", pendingPayment.getTotalAmount(),
                                                    "involvedAccounts", pendingPayment.getAccounts(), // Lista conturilor implicate
                                                    "splitPaymentType", splitPaymentType,
                                                    "error", "Account " + errorIban + " has insufficient funds for a split payment."
                                            );

                                            InsufficientFundsSplitEqualTransaction transaction = (InsufficientFundsSplitEqualTransaction)
                                                    CreateTransaction.getInstance().createTransaction("InsufficientFundsSplitEqualTransaction", params);

                                            getTransactions().add(transaction);
                                        }
                                        getTransactions().sort((t1, t2) -> Integer.compare(t1.getTimestamp(), t2.getTimestamp()));
                                    }
                                }
                                pendingPayment.markAsCompleted();
                            } else {
                                if ("custom".equals(splitPaymentType)) {
                                    processSplitPaymentCustom(pendingPayment);
                                } else if ("equal".equals(splitPaymentType)) {
                                    double amountPerUser = pendingPayment.getTotalAmount() / pendingPayment.getAccounts().size();
                                    processSplitPaymentEqual(pendingPayment, amountPerUser);
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    private void handleNewSplitPayment(List<String> accounts, double amountPerAccount) {
        double totalAmount = getCommand().getAmount();
        String currency = getCommand().getCurrency();
        String splitPaymentType = getCommand().getSplitPaymentType();
        int timestamp = getCommand().getTimestamp();

        HashMap<String, Boolean> approvalStatus = new HashMap<>();
        for (String iban : accounts) {
            approvalStatus.put(iban, false);
        }

        QueueSplitPayment pendingPayment;
        if ("equal".equals(splitPaymentType)) {
            double amountPerUser = totalAmount / accounts.size();
            List<Double> amountsForUsers = new ArrayList<>();
            for (int i = 0; i < accounts.size(); i++) {
                amountsForUsers.add(amountPerUser);
            }

            pendingPayment = new QueueSplitPayment(
                    accounts, amountsForUsers, totalAmount, currency, approvalStatus, timestamp, splitPaymentType
            );
        } else { // "custom"
            List<Double> amountsForUsers = getCommand().getAmountForUsers();
            if (amountsForUsers.size() != accounts.size()) {
                System.out.println("Number of amounts does not match number of accounts. Aborting.");
                return;
            }

            pendingPayment = new QueueSplitPayment(
                    accounts, amountsForUsers, totalAmount, currency, approvalStatus, timestamp, splitPaymentType
            );
        }

        getQueueSplitPayments().add(pendingPayment);
    }

    private void processSplitPaymentCustom(QueueSplitPayment pendingPayment) {
        List<String> accounts = pendingPayment.getAccounts();
        List<Double> amounts = pendingPayment.getAmounts();
        String currency = pendingPayment.getCurrency();

        for (int i = 0; i < accounts.size(); i++) {
            String iban = accounts.get(i);
            Account account = findAccountByIBAN(iban);
            if (account != null) {
                double requiredAmount = getGraph().convertCurrency(amounts.get(i), currency, account.getCurrency());
                account.setBalance(account.getBalance() - requiredAmount);
                User user = findUserByIBAN(iban);
                if (user != null) {
                    Map<String, Object> params = Map.of(
                            "description", String.format("Split payment of %.2f %s", pendingPayment.getTotalAmount(), currency),
                            "timestamp", pendingPayment.getTimestamp(),
                            "email", user.getUser().getEmail(),
                            "amountsForUsers", amounts, // Lista de sume pentru utilizatori
                            "currency", currency,
                            "involvedAccounts", accounts, // Lista conturilor implicate
                            "splitPaymentType", "custom"
                    );

                    SplitPaymentCustomGoodTransaction transaction = (SplitPaymentCustomGoodTransaction)
                            CreateTransaction.getInstance().createTransaction("SplitPaymentCustomGoodTransaction", params);

                    getTransactions().add(transaction);


                    getTransactions().sort((t1, t2) -> Integer.compare(t1.getTimestamp(), t2.getTimestamp()));
                }
            }
        }
        pendingPayment.markAsCompleted();
    }

    private void processSplitPaymentEqual(QueueSplitPayment pendingPayment, double amountPerUser) {
        List<String> accounts = pendingPayment.getAccounts();
        String currency = pendingPayment.getCurrency();

        for (String iban : accounts) {
            Account account = findAccountByIBAN(iban);
            if (account != null) {
                double requiredAmount = getGraph().convertCurrency(amountPerUser, currency, account.getCurrency());
                account.setBalance(account.getBalance() - requiredAmount);
                User user = findUserByIBAN(iban);
                if (user != null) {
//                    Transaction transaction = new Transaction(
//                            List.of(amountPerUser),
//                            currency,
//                            pendingPayment.getTotalAmount(),
//                            accounts,
//                            "equal",
//                            pendingPayment.getTimestamp(),
//                            user.getUser().getEmail()
//                    );
//                    getTransactions().add(transaction);
                    getTransactions().sort((t1, t2) -> Integer.compare(t1.getTimestamp(), t2.getTimestamp()));
                }
            }
        }
        pendingPayment.markAsCompleted();
    }

    private User findUserByIBAN(String iban) {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(iban)) {
                    return user;
                }
            }
        }
        return null;
    }

    private Account findAccountByIBAN(String iban) {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    public void undo() {
    }
}
