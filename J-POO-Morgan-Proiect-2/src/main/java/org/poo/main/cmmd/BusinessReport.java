package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.BusinessAccount;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

/**
 * The BusinessReport class is responsible for generating a report for a business account.
 * It includes details such as the account balance, deposit limit, spending limit,
 * and statistics about employees and managers associated with the business account.
 */
public class BusinessReport extends Command {

    /**
     * Constructs a BusinessReport command with the specified parameters.
     *
     * @param users            The list of users in the system.
     * @param commandNode      The ObjectNode representing the command node.
     * @param output           The ArrayNode where the output of the command will be added.
     * @param command          The input command details.
     * @param objectMapper     The ObjectMapper used for creating JSON nodes.
     * @param transactions     The list of transactions in the system.
     * @param businessAccounts The list of business accounts in the system.
     */
    public BusinessReport(final ArrayList<User> users, final ObjectNode commandNode,
                          final ArrayNode output, final CommandInput command,
                          final ObjectMapper objectMapper,
                          final ArrayList<Transaction> transactions,
                          final ArrayList<BusinessAccount> businessAccounts) {
        super(users, commandNode, output, command, objectMapper, null,
                transactions, null, null, null, businessAccounts);
    }

    /**
     * Executes the business report command.
     * Generates a detailed report for the specified business account, including information
     * about its balance, currency, deposit limit, spending limit, employees, managers,
     * and total deposited and spent amounts.
     */
    @Override
    public void execute() {
        for (BusinessAccount account : getBusinessAccounts()) {
            if (account.getAccountIban().equals(getCommand().getAccount())) {
                ObjectNode reportNode = getObjectMapper().createObjectNode();

                // Add account details to the report
                reportNode.put("balance", account.getBalance());
                reportNode.put("currency", account.getCurrency());
                reportNode.put("deposit limit", account.getDepositLimit());
                reportNode.put("spending limit", account.getSpendingLimit());
                reportNode.put("statistics type", "transaction");
                reportNode.put("IBAN", account.getAccountIban());

                double totalDeposited = 0;
                double totalSpent = 0;

                // Add employees' details to the report
                ArrayNode employeesArray = getObjectMapper().createArrayNode();
                for (User employee : account.getEmployees()) {
                    ObjectNode employeeNode = getObjectMapper().createObjectNode();
                    employeeNode.put("username", employee.getFullNameFromEmail());
                    employeeNode.put("deposited", employee.getDeposited());
                    employeeNode.put("spent", employee.getSpent());

                    totalDeposited += employee.getDeposited();
                    totalSpent += employee.getSpent();

                    employeesArray.add(employeeNode);
                }
                reportNode.set("employees", employeesArray);

                // Add managers' details to the report
                ArrayNode managersArray = getObjectMapper().createArrayNode();
                for (User manager : account.getManagers()) {
                    ObjectNode managerNode = getObjectMapper().createObjectNode();
                    managerNode.put("username", manager.getFullNameFromEmail());
                    managerNode.put("deposited", manager.getDeposited());
                    managerNode.put("spent", manager.getSpent());

                    totalDeposited += manager.getDeposited();
                    totalSpent += manager.getSpent();

                    managersArray.add(managerNode);
                }
                reportNode.set("managers", managersArray);

                // Add total statistics to the report
                reportNode.put("total deposited", totalDeposited);
                reportNode.put("total spent", totalSpent);

                // Add report to the output
                ObjectNode commandNode = getObjectMapper().createObjectNode();
                commandNode.put("command", "businessReport");
                commandNode.put("timestamp", getCommand().getTimestamp());
                commandNode.set("output", reportNode);

                getOutput().add(commandNode);
                return;
            }
        }
    }

    /**
     * Undo method for future development.
     * This method is currently a placeholder and does not perform any action.
     */
    @Override
    public void undo() {
    }
}
