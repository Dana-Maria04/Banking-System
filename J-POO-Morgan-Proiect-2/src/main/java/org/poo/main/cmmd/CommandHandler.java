package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.userinfo.Commerciant;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.QueueSplitPayment;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.BusinessAccount;
import org.poo.fileio.ExchangeInput;

import org.poo.main.userinfo.transactions.PayOnlineTransaction;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Is responsible for processing commands.
 * It handles the execution of various commands by invoking the appropriate
 * logic for each command type.
 */
public class CommandHandler {

    /**
     * This method processes a list of commands, executing each one in the given order.
     * It creates necessary objects such as users, exchange graphs, and transactions based on
     * the input data.
     *
     * @param objectInput The input object containing the users, commands, and exchange rates data
     * @param output      The array node where the command results will be written
     * @return The output array containing the results of the executed commands
     */
    public ArrayNode handle(final ObjectInput objectInput, final ArrayNode output) {
        ArrayList<User> users = new ArrayList<>();
        for (UserInput userInput : objectInput.getUsers()) {
            User user = new User(new ArrayList<>());
            user.setUser(userInput);
            user.setTransactions(new ArrayList<>());
            if (userInput.getOccupation().equals("student")) {
                user.setUserPlan("student");
            } else {
                user.setUserPlan("standard");
            }


            users.add(user);
        }

        ExchangeGraph graph = createExchangeGraphFromInputs(objectInput);

        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<PayOnlineTransaction> spendingsReportTransactions = new ArrayList<>();
        ArrayList<CommandInput> commands = new ArrayList<>(Arrays.asList(objectInput
                                                                .getCommands()));

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Commerciant> commerciants = new ArrayList<>();
        for (CommerciantInput commerciantInput : objectInput.getCommerciants()) {
            Commerciant commerciant = new Commerciant();
            commerciant.getCommerciant().setCommerciant(commerciantInput.getCommerciant());
            commerciant.getCommerciant().setId(commerciantInput.getId());
            commerciant.getCommerciant().setType(commerciantInput.getType());
            commerciant.getCommerciant().setCashbackStrategy(
                    commerciantInput.getCashbackStrategy());
            commerciant.getCommerciant().setAccount(commerciantInput.getAccount());
            commerciants.add(commerciant);
        }
        ArrayList<QueueSplitPayment> queueSplitPayments = new ArrayList<>();

        ArrayList<BusinessAccount> businessAccounts = new ArrayList<>();

        Utils.resetRandom();

        // Process each command based on its type

        for (CommandInput cmd : commands) {
            processCommand(cmd, users, graph, transactions, businessAccounts, queueSplitPayments,
                    commerciants, spendingsReportTransactions, output, objectMapper);
        }

        return output;
    }

    /**
     * Creates an ExchangeGraph instance from the given object input.
     * This method processes the exchange rates input and constructs the graph.
     *
     * @param objectInput The input object containing exchange rate data
     * @return A constructed ExchangeGraph instance
     */
    private ExchangeGraph createExchangeGraphFromInputs(final ObjectInput objectInput) {
        ArrayList<ExchangeInput> exchangeRates = new ArrayList<>();
        for (ExchangeInput exchangeInput : objectInput.getExchangeRates()) {
            ExchangeInput exchange = new ExchangeInput();
            exchange.setTo(exchangeInput.getTo());
            exchange.setRate(exchangeInput.getRate());
            exchange.setFrom(exchangeInput.getFrom());
            exchange.setTimestamp(exchangeInput.getTimestamp());
            exchangeRates.add(exchange);
        }
        return new ExchangeGraph(exchangeRates);
    }

    /**
     * Processes a single command based on its type.
     *
     * @param cmd              The command input to be processed.
     * @param users            The list of users.
     * @param graph            The exchange graph for currency conversion.
     * @param transactions     The list of transactions.
     * @param businessAccounts The list of business accounts.
     * @param queueSplitPayments The list of queued split payments.
     * @param commerciants     The list of commerciants.
     * @param spendingsReportTransactions The list of spendings report transactions.
     * @param output           The output node where results will be written.
     * @param objectMapper     ObjectMapper for JSON operations.
     */
    private void processCommand(final CommandInput cmd, final ArrayList<User> users,
                                final ExchangeGraph graph,
                                final ArrayList<Transaction> transactions,
                                final ArrayList<BusinessAccount> businessAccounts,
                                final ArrayList<QueueSplitPayment> queueSplitPayments,
                                final ArrayList<Commerciant> commerciants,
                                final ArrayList<PayOnlineTransaction> spendingsReportTransactions,
                                final ArrayNode output, final ObjectMapper objectMapper) {

        ObjectNode commandNode = objectMapper.createObjectNode();

        switch (cmd.getCommand()) {
            case "printUsers":
                new PrintUsers(users, commandNode, output, cmd, objectMapper).execute();
                break;
            case "addFunds":
                new AddFunds(users, commandNode, output, cmd,
                        objectMapper, businessAccounts, graph).execute();
                break;
            case "createCard":
                new CreateCard(users, cmd, 0, transactions, businessAccounts).execute();
                break;
            case "addAccount":
                new AddAccount(users, commandNode, output, cmd, objectMapper,
                        transactions, businessAccounts, graph).execute();
                break;
            case "deleteAccount":
                new DeleteAccount(users, commandNode, output, cmd, objectMapper,
                        transactions).execute();
                break;
            case "createOneTimeCard":
                new CreateCard(users, cmd, 1, transactions,
                        businessAccounts).execute();
                break;
            case "deleteCard":
                new DeleteCard(users, cmd, transactions).execute();
                break;
            case "setMinimumBalanmce":
                new SetMinimumBalance(users, cmd).execute();
                break;
            case "payOnline":
                new PayOnline(users, cmd, graph, output, objectMapper, commandNode, transactions,
                        spendingsReportTransactions, commerciants, businessAccounts).execute();
                break;
            case "sendMoney":
                new SendMoney(users, cmd, graph, output, objectMapper, commandNode,
                        transactions, businessAccounts).execute();
                break;
            case "printTransactions":
                new PrintTransactions(users, commandNode, output, cmd, objectMapper,
                        transactions).execute();
                break;
            case "checkCardStatus":
                new CheckCardStatus(users, commandNode, output, cmd, objectMapper,
                        transactions).execute();
                break;
            case "splitPayment":
                new SplitPayment(graph, users, commandNode, output, cmd, objectMapper,
                        transactions, queueSplitPayments).execute();
                break;
            case "report":
                new Report(users, cmd, graph, output, objectMapper, commandNode,
                        transactions).execute();
                break;
            case "spendingsReport":
                new SpendingsReport(users, commandNode, output, cmd, objectMapper,
                        transactions, spendingsReportTransactions).execute();
                break;
            case "addInterest":
                new AddInterestRate(users, commandNode, output, cmd, objectMapper,
                        transactions).execute();
                break;
            case "changeInterestRate":
                new ChangeInterestRate(users, commandNode, output, cmd, objectMapper,
                        transactions).execute();
                break;
            case "withdrawSavings":
                new WithdrawSavings(graph, cmd, transactions, users).execute();
                break;
            case "upgradePlan":
                new UpgradePlan(graph, cmd, transactions, users, objectMapper, output).execute();
                break;
            case "cashWithdrawal":
                new CashWithdrawal(users, commandNode, output, cmd, objectMapper,
                        transactions, graph).execute();
                break;
            case "acceptSplitPayment":
                new SplitPayment(graph, users, commandNode, output, cmd, objectMapper,
                        transactions, queueSplitPayments).execute();
                break;
            case "addNewBusinessAssociate":
                new AddNewBusinessAssociate(users, commandNode, output, cmd, objectMapper,
                        transactions, businessAccounts).execute();
                break;
            case "businessReport":
                new BusinessReport(users, commandNode, output, cmd, objectMapper,
                        transactions, businessAccounts).execute();
                break;
            case "changeSpendingLimit":
                new ChangeSpendingLimit(users, cmd, transactions, businessAccounts,
                        objectMapper, output).execute();
                break;
            case "changeDepositLimit":
                new ChangeDepositLimit(users, cmd, transactions, businessAccounts,
                        objectMapper, output).execute();
                break;
            case "rejectSplitPayment":
                new SplitPayment(graph, users, commandNode, output, cmd, objectMapper,
                        transactions, queueSplitPayments).execute();
                break;
            default:
                break;
        }
    }

}
