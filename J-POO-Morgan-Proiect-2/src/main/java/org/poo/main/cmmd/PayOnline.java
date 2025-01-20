package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.Commerciant;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

/**
 * The PayOnline class handles the execution of the "payOnline" command.
 * It converts the specified amount to the account's currency,
 * performing the payment, and logging the transaction.
 */
public class PayOnline extends Command {

    /**
     * Constructs a PayOnline command with the specified parameters.
     *
     * @param users                    The list of users
     * @param command                  The command input
     * @param exchangeGraph            The exchange graph used for currency conversion
     * @param output                   The array node to store the output
     * @param objectMapper             ObjectMapper for JSON operations
     * @param commandNode              The command node containing information about the command
     * @param transactions             The list of transactions to log the action
     * @param payOnlineTransactions    The list of transactions related to pay online actions
     */
    public PayOnline(final ArrayList<User> users, final CommandInput command,
                     final ExchangeGraph exchangeGraph, final ArrayNode output,
                     final ObjectMapper objectMapper, final ObjectNode commandNode,
                     final ArrayList<Transaction> transactions,
                     final ArrayList<PayOnlineTransaction> payOnlineTransactions,
                     final ArrayList<Commerciant> commerciants) {
        super(users, commandNode, output, command, objectMapper,
                exchangeGraph, transactions, payOnlineTransactions, commerciants);
    }

    /**
     * Executes the payOnline command by processing the payment for the specified user and account.
     * The specified amount is converted to the account's currency and the payment is processed.
     * If the card is found, the payment is completed, otherwise, an error message is returned.
     */
    @Override
    public void execute() {

        final ExchangeGraph exchangeGraph = getGraph();

        Commerciant commerciant = new Commerciant();
        for(Commerciant commerciant1 : getCommerciants()) {
            if(commerciant1.getCommerciant().getCommerciant().equals(getCommand().getCommerciant())) {
                commerciant = commerciant1;
                break;
            }
        }




        for (final User user : getUsers()) {
            if (user.getUser().getEmail().equals(getCommand().getEmail())) {
                for (final Account account : user.getAccounts()) {

                    final double convertedAmount = exchangeGraph.convertCurrency(
                            getCommand().getAmount(),
                            getCommand().getCurrency(),
                            account.getCurrency()
                    );


                    account.pay(convertedAmount, getCommand().getCardNumber(),
                            account.getAccountCards(), user, getCommand(), getTransactions(),
                            account.getAccountIban(),
                            getSpendingsReportTransactions(), account, commerciant,
                            exchangeGraph);
                    if (account.getFoundCard() == 1 || account.getInsufficientFunds() == 1) {
                        return;
                    }
                }
            }
        }
        final Account tempAccount = new Account();
        tempAccount.addResponseToOutput(
                getObjectMapper(),
                getCommandNode(),
                getOutput(),
                getCommand(),
                "Card not found"
        );
    }


    /**
     * For future development
     */
    @Override
    public void undo() {
    }
}
