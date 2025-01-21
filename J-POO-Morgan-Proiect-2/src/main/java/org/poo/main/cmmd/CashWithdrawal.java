package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.*;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * Withdraws an amount from a savings account to a classic account.
 */
public class CashWithdrawal extends Command {

    /**
     * Constructs a CashWithdrawal command.
     *
     * @param users        The list of users in the system.
     * @param commandNode  The ObjectNode containing details about the command.
     * @param output       The ArrayNode where the results of the command will be stored.
     * @param command      The input command containing the withdrawal details.
     * @param objectMapper The ObjectMapper instance for JSON operations.
     * @param transactions The list of transactions for logging purposes.
     * @param graph        The ExchangeGraph instance for handling currency conversions.
     */
    public CashWithdrawal(final ArrayList<User> users, final ObjectNode commandNode,
                          final ArrayNode output, final CommandInput command,
                          final ObjectMapper objectMapper,
                          final ArrayList<Transaction> transactions,
                          final ExchangeGraph graph) {
        super(users, commandNode, output, command, objectMapper, graph,
                transactions, null, null, null, null);
    }

    /**
     * Verifies if the card exists,
     * checks if the account has sufficient funds, calculates comisions based on the user's plan,
     * and logs the transaction. If the card is not found or the account has insufficient funds,
     * an appropriate error message is added to the output.
     */
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
                            double commission = amount * Constants.STANDARD_CASHBACK_2 ;
                            commission = getGraph().convertCurrency(commission, "RON",
                                    account.getCurrency());
                            account.setBalance(account.getBalance() - commission);
                        }

                        if (user.getUserPlan().equals("silver")
                                && amount >= Constants.THRESHOLD_3) {
                            double commission = amount * Constants.STANDARD_CASHBACK_1;
                            commission = getGraph().convertCurrency(commission, "RON",
                                    account.getCurrency());
                            account.setBalance(account.getBalance() - commission);
                        }

                        double withdrawalAmount = getGraph().convertCurrency(amount, "RON",
                                account.getCurrency());
                        account.setBalance(account.getBalance() - withdrawalAmount + cashback);

                        Map<String, Object> params = Map.of(
                                "description", "Cash withdrawal of "
                                        + getCommand().getAmount(),
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

    /**
     * Reserved for future development to undo a cash withdrawal transaction.
     */
    @Override
    public void undo() {

    }
}
