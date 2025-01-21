package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Constants;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.CreateTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Map;

/**
 * The UpgradePlan class is responsible for handling user plan upgrades.
 * It verifies if the user has sufficient funds in their account to upgrade
 * and performs the upgrade if the conditions are met.
 */
public class UpgradePlan extends Command {

    /**
     * Constructs an UpgradePlan command.
     *
     * @param graph        The exchange graph used for currency conversion.
     * @param commandInput The input command containing details about the upgrade request.
     * @param transactions The list of transactions for logging purposes.
     * @param users        The list of users in the system.
     * @param objectMapper The ObjectMapper used for JSON operations.
     * @param output       The ArrayNode where the output will be written.
     */
    public UpgradePlan(final ExchangeGraph graph, final CommandInput commandInput,
                       final ArrayList<Transaction> transactions, final ArrayList<User> users,
                       final ObjectMapper objectMapper, final ArrayNode output) {
        super(users, null, output, commandInput, objectMapper,
                graph, transactions, null, null, null, null);
    }

    /**
     * Executes the plan upgrade command.
     * It validates whether the user meets the conditions to upgrade their plan,
     * such as having sufficient funds. If the upgrade is successful, it deducts
     * the required amount from the user's account and logs the transaction.
     * If conditions are not met, it logs an error message.
     */
    @Override
    public void execute() {
        boolean accountFound = false;

        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIban().equals(getCommand().getAccount())) {
                    accountFound = true;

                    // Handle plan upgrades
                    if ((user.getUserPlan().equals("standard")
                            || user.getUserPlan().equals("student"))
                            && getCommand().getNewPlanType().equals("silver")) {

                        processPlanUpgrade(user, account, Constants.UPGRADE_1, "silver");

                    } else if (user.getUserPlan().equals("silver")
                            && getCommand().getNewPlanType().equals("gold")) {

                        processPlanUpgrade(user, account, Constants.UPGRADE_2, "gold");

                    } else if ((user.getUserPlan().equals("standard")
                            || user.getUserPlan().equals("student"))
                            && getCommand().getNewPlanType().equals("gold")) {

                        processPlanUpgrade(user, account, Constants.UPGRADE_3, "gold");
                    }
                }
            }
        }

        // If the account was not found, log an error
        if (!accountFound) {
            ObjectNode outputNode = getObjectMapper().createObjectNode();
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", getCommand().getTimestamp());

            ObjectNode commandNode = getObjectMapper().createObjectNode();
            commandNode.put("command", "upgradePlan");
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", getCommand().getTimestamp());

            getOutput().add(commandNode);
        }
    }

    /**
     * Processes the plan upgrade by checking if the user has sufficient funds
     * and performing the upgrade if possible.
     *
     * @param user       The user requesting the upgrade.
     * @param account    The account from which the upgrade fee will be deducted.
     * @param ronAmount  The fee in RON required for the upgrade.
     * @param newPlan    The new plan type the user is upgrading to.
     */
    private void processPlanUpgrade(final User user, final Account account,
                                    final double ronAmount, final String newPlan) {
        double rightAmount = getGraph().convertCurrency(ronAmount, "RON", account.getCurrency());

        if (account.getBalance() - account.getMinimumBalance() >= rightAmount) {
            account.setBalance(account.getBalance() - rightAmount);

            Map<String, Object> params = Map.of(
                    "description", "Upgrade plan",
                    "timestamp", getCommand().getTimestamp(),
                    "email", user.getUser().getEmail(),
                    "newPlanType", getCommand().getNewPlanType(),
                    "iban", account.getAccountIban()
            );
            Transaction transaction = CreateTransaction.getInstance()
                    .createTransaction("UpgradePlan", params);
            getTransactions().add(transaction);

            user.setUserPlan(newPlan);
        } else {
            Map<String, Object> insufficientFundsParams = Map.of(
                    "description", "Insufficient funds",
                    "timestamp", getCommand().getTimestamp(),
                    "email", user.getUser().getEmail()
            );

            Transaction insufficientFundsTransaction =
                    CreateTransaction.getInstance()
                            .createTransaction(
                                    "InsufficientFundsTransaction", insufficientFundsParams);

            getTransactions().add(insufficientFundsTransaction);
        }
    }

    /**
     * Reserved for future development to undo a plan upgrade transaction.
     */
    @Override
    public void undo() {

    }
}
