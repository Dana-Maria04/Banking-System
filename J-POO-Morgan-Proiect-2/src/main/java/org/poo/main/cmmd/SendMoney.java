package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.*;
import org.poo.main.userinfo.transactions.MoneyTransferTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.main.userinfo.transactions.CreateTransaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * The SendMoney class represents a command to transfer money between two accounts.
 * It handles the validation of sufficient funds, conversion of currency, and transaction logging.
 */
public class SendMoney extends Command {

    /**
     * Constructs a SendMoney command with the provided parameters.
     *
     * @param users                The list of users.
     * @param command             The command input data.
     * @param exchangeGraph       The graph for currency exchange rates.
     * @param output              The array node where the output will be stored.
     * @param objectMapper        ObjectMapper for JSON operations
     * @param commandNode         The command node containing information about the command.
     * @param transactions        The list of transactions that will be updated.
     */
    public SendMoney(final ArrayList<User> users, final CommandInput command,
                     final ExchangeGraph exchangeGraph, final ArrayNode output,
                     final ObjectMapper objectMapper, final ObjectNode commandNode,
                     final ArrayList<Transaction> transactions,
                     final ArrayList<BusinessAccount> businessAccounts) {
        super(users, commandNode, output, command, objectMapper, exchangeGraph, transactions,
                null, null, null, businessAccounts);
    }

    /**
     * Executes the money transfer command. It verifies if the sender has sufficient funds,
     * performs the currency conversion, and logs the transaction for both the sender and receiver.
     */
    @Override
    public void execute() {

        BusinessAccount senderBusinessAccount = null;
        for (BusinessAccount businessAccount : getBusinessAccounts()) {
            if (businessAccount.getAccountIban().equals(getCommand().getAccount())) {
                senderBusinessAccount = businessAccount;
                break;
            }
        }

        final ExchangeGraph exchangeGraph = getGraph();

        Account receiver = new Account();
        User receiverUser = new User();
        for (final User user : getUsers()) {
            receiver = Account.searchAccount(getCommand().getReceiver(), user.getAccounts());
            if (receiver != null) {
                receiverUser = user;
                break;
            }
        }

        Account sender = new Account();
        User senderUser = new User();
        for (final User user : getUsers()) {
            sender = Account.searchAccount(getCommand().getAccount(), user.getAccounts());
            if (sender != null) {
                senderUser = user;
                break;
            }
        }


        if (senderBusinessAccount != null && receiver != null) {
            if (handleBusinessAccountTransfer(senderBusinessAccount, receiver)) {
                return;
            }
        }

        if (receiver == null || sender == null) {

            ObjectNode errorNode = getObjectMapper().createObjectNode();
            errorNode.put("timestamp", getCommand().getTimestamp());
            errorNode.put("description", "User not found");
            getCommandNode().set("output", errorNode);
            getCommandNode().put("command", "sendMoney");
            getCommandNode().put("timestamp", getCommand().getTimestamp());
            getOutput().add(getCommandNode());
            return;
        }

        if (sender.getBalance() < getCommand().getAmount()) {
            // Log the insufficient funds transaction
            final Map<String, Object> insufficientFundsParams = createTransactionParams(
                    "Insufficient funds",
                    0.0,
                    sender.getAccountIban(),
                    receiver.getAccountIban(),
                    "failed",
                    sender.getCurrency(),
                    sender.getAccountIban()
            );

            final Transaction transaction = CreateTransaction.getInstance()
                    .createTransaction("MoneyTransfer", insufficientFundsParams);
            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }
        // Check if the sender's balance is below the minimum required balance after the transfer
        if (sender.getMinimumBalance() >= sender.getBalance() - getCommand().getAmount()) {
            final Map<String, Object> belowMinimumBalanceParams = createTransactionParams(
                    "Insufficient funds",
                    0.0,
                    sender.getAccountIban(),
                    receiver.getAccountIban(),
                    "failed",
                    sender.getCurrency(),
                    sender.getAccountIban()
            );


            final Transaction transaction = CreateTransaction.getInstance()
                    .createTransaction("MoneyTransfer", belowMinimumBalanceParams);
            transaction.setEmail(senderUser.getUser().getEmail());
            getTransactions().add(transaction);
            return;
        }

        final double amount = exchangeGraph.convertCurrency(getCommand().getAmount(),
                sender.getCurrency(), receiver.getCurrency());

        final Map<String, Object> senderParams = createTransactionParams(
                getCommand().getDescription(),
                getCommand().getAmount(),
                sender.getAccountIban(),
                receiver.getAccountIban(),
                "sent",
                sender.getCurrency(),
                sender.getAccountIban()
        );

        final MoneyTransferTransaction senderTransaction = (MoneyTransferTransaction)
                CreateTransaction.getInstance()
                .createTransaction("MoneyTransfer", senderParams);

        senderTransaction.setEmail(senderUser.getUser().getEmail());

        final Map<String, Object> receiverParams = createTransactionParams(
                getCommand().getDescription(),
                amount,
                sender.getAccountIban(),
                receiver.getAccountIban(),
                "received",
                receiver.getCurrency(),
                receiver.getAccountIban()
        );


        final MoneyTransferTransaction receiverTransaction = (MoneyTransferTransaction)
                CreateTransaction.getInstance()
                .createTransaction("MoneyTransfer", receiverParams);

        receiverTransaction.setEmail(receiverUser.getUser().getEmail());


        getTransactions().add(senderTransaction);
        getTransactions().add(receiverTransaction);

        if (senderUser.getUserPlan().equals("standard")) {
            double comision = Constants.STANDARD_CASHBACK_2 * amount;
            double sum = exchangeGraph.convertCurrency(comision, "RON", sender.getCurrency());
            sender.decBalance(sum);
        }


        double amountInRON = exchangeGraph.convertCurrency(amount, sender.getCurrency(), "RON");
        if (amountInRON >= Constants.THRESHOLD_3  && senderUser.getUserPlan().equals("silver")) {
            double comision = Constants.STANDARD_CASHBACK_1 * amountInRON;
            double sum = exchangeGraph.convertCurrency(comision, "RON", sender.getCurrency());
            sender.decBalance(sum);
        }
        receiver.incBalance(amount);
        sender.decBalance(getCommand().getAmount());
    }
    /**
     * Constructs a map of transaction parameters used for creating transactions.
     * This method helps reduce duplication
     *
     * @param description The description of the transaction
     * @param amount The amount to be transferred or involved in the transaction.
     * @param senderIban The IBAN of the sender's account.
     * @param receiverIban The IBAN of the receiver's account.
     * @param status The status of the transaction
     * @param currency The currency of the transaction
     * @param accountIban The IBAN associated with the account initiating the transaction.
     * @return A map containing all the transaction parameters, which can be used for
     *         creating a transaction.
     */
    private Map<String, Object> createTransactionParams(final String description,
                                                        final double amount,
                                                        final String senderIban,
                                                        final String receiverIban,
                                                        final String status, final String currency,
                                                        final String accountIban) {
        return constructParams(
                description,
                Map.of(
                    "amount", amount,
                    "currency", currency,
                    "senderIban", senderIban,
                    "receiverIban", receiverIban,
                    "status", status,
                    "accountIban", accountIban
                )
        );
    }

    /**
     * Handles the money transfer for business accounts.
     *
     * @param senderBusinessAccount The sender's business account.
     * @param receiver              The receiver's account.
     * @return true if the transfer was handled, false otherwise.
     */
    private boolean handleBusinessAccountTransfer(final BusinessAccount senderBusinessAccount,
                                                  final Account receiver) {
        boolean isEmployee = false;
        boolean isManager = false;

        for (User employee : senderBusinessAccount.getEmployees()) {
            if (employee.getUser().getEmail().equals(getCommand().getEmail())) {
                isEmployee = true;
                break;
            }
        }

        for (User manager : senderBusinessAccount.getManagers()) {
            if (manager.getUser().getEmail().equals(getCommand().getEmail())) {
                isManager = true;
                break;
            }
        }

        double amount = getCommand().getAmount();
        double amountInRON = getGraph().convertCurrency(amount,
                senderBusinessAccount.getCurrency(), "RON");

        if (isEmployee && amountInRON > Constants.THRESHOLD_3) {
            ObjectNode errorNode = getObjectMapper().createObjectNode();
            errorNode.put("timestamp", getCommand().getTimestamp());
            errorNode.put("description", "Employees cannot spend more than 500 RON.");
            getCommandNode().set("output", errorNode);
            getCommandNode().put("command", "sendMoney");
            getCommandNode().put("timestamp", getCommand().getTimestamp());
            getOutput().add(getCommandNode());
            return true;
        }

        if (senderBusinessAccount.getBalance() < amount
                || senderBusinessAccount.getBalance() - amount
                        < senderBusinessAccount.getMinimumBalance()) {

            ObjectNode errorNode = getObjectMapper().createObjectNode();
            errorNode.put("timestamp", getCommand().getTimestamp());
            errorNode.put("description", "Insufficient funds or below minimum balance.");
            getCommandNode().set("output", errorNode);
            getCommandNode().put("command", "sendMoney");
            getCommandNode().put("timestamp", getCommand().getTimestamp());
            getOutput().add(getCommandNode());
            return true;
        }

        double convertedAmount = getGraph().convertCurrency(
                amount,
                senderBusinessAccount.getCurrency(),
                receiver.getCurrency()
        );

        senderBusinessAccount.setBalance(senderBusinessAccount.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + convertedAmount);

        return true;
    }



    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
