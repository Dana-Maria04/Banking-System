package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.*;
import org.poo.main.userinfo.Commerciant;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
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
            if(userInput.getOccupation().equals("student"))
                user.setUserPlan("student");
            else
                user.setUserPlan("standard");

            users.add(user);
        }

        ExchangeGraph graph = createExchangeGraphFromInputs(objectInput);

        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<PayOnlineTransaction> spendingsReportTransactions = new ArrayList<>();
        ArrayList<CommandInput> commands = new ArrayList<>(Arrays.asList(objectInput
                                                                .getCommands()));

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Commerciant> commerciants = new ArrayList<>();
        for(CommerciantInput commerciantInput : objectInput.getCommerciants()){
            Commerciant commerciant = new Commerciant();
            commerciant.getCommerciant().setCommerciant(commerciantInput.getCommerciant());
            commerciant.getCommerciant().setId(commerciantInput.getId());
            commerciant.getCommerciant().setType(commerciantInput.getType());
            commerciant.getCommerciant().setCashbackStrategy(commerciantInput.getCashbackStrategy());
            commerciant.getCommerciant().setAccount(commerciantInput.getAccount());
            commerciants.add(commerciant);
        }


        Utils.resetRandom();

        // Process each command based on its type
        for (CommandInput cmd : commands) {
            ObjectNode commandNode = objectMapper.createObjectNode();

            switch (cmd.getCommand()) {
                case "printUsers":
                    PrintUsers printUsers = new PrintUsers(users, commandNode, output,
                            cmd, objectMapper);
                    printUsers.execute();
                    break;
                case "addFunds":
                    AddFunds addFunds = new AddFunds(users, commandNode, output, cmd,
                            objectMapper);
                    addFunds.execute();
                    break;
                case "createCard":
                    CreateCard createCard = new CreateCard(users, cmd, 0, transactions);
                    createCard.execute();
                    break;
                case "addAccount":
                    AddAccount addAccount = new AddAccount(users, commandNode, output, cmd,
                            objectMapper, transactions);
                    addAccount.execute();
                    break;
                case "deleteAccount":
                    DeleteAccount deleteAccount = new DeleteAccount(users, commandNode, output,
                            cmd, objectMapper, transactions);
                    deleteAccount.execute();
                    break;
                case "createOneTimeCard":
                    CreateCard createOneTimeCard = new CreateCard(users, cmd, 1, transactions);
                    createOneTimeCard.execute();
                    break;
                case "deleteCard":
                    DeleteCard deleteCard = new DeleteCard(users, cmd, transactions);
                    deleteCard.execute();
                    break;
                case "setMinimumBalanmce":
                    SetMinimumBalance setMinimumBalance = new SetMinimumBalance(users, cmd);
                    setMinimumBalance.execute();
                    break;
                case "payOnline":
                    PayOnline payOnline = new PayOnline(users, cmd, graph, output,
                            objectMapper, commandNode, transactions,
                            spendingsReportTransactions, commerciants);
                    payOnline.execute();
                    break;
                case "sendMoney":
                    SendMoney sendMoney = new SendMoney(users, cmd, graph, output, objectMapper,
                            commandNode, transactions);
                    sendMoney.execute();
                    break;
                case "printTransactions":
                    PrintTransactions printTransactions = new PrintTransactions(users, commandNode,
                            output, cmd, objectMapper, transactions);
                    printTransactions.execute();
                    break;
                case "checkCardStatus":
                    CheckCardStatus checkCardStatus = new CheckCardStatus(users, commandNode,
                            output, cmd, objectMapper, transactions);
                    checkCardStatus.execute();
                    break;
                case "splitPayment":
                    SplitPayment splitPayment = new SplitPayment(graph, users, commandNode, output,
                            cmd, objectMapper, transactions);
                    splitPayment.execute();
                    break;
                case "report":
                    Report report = new Report(users, cmd, graph, output, objectMapper,
                            commandNode, transactions);
                    report.execute();
                    break;
                case "spendingsReport":
                    SpendingsReport spendingReport = new SpendingsReport(users, commandNode,
                            output, cmd, objectMapper, transactions, spendingsReportTransactions);
                    spendingReport.execute();
                    break;
                case "addInterest":
                    AddInterestRate addInterest = new AddInterestRate(users, commandNode, output,
                            cmd, objectMapper, transactions);
                    addInterest.execute();
                    break;
                case "changeInterestRate":
                    ChangeInterestRate changeInterestRate = new ChangeInterestRate(users,
                            commandNode, output, cmd, objectMapper, transactions);
                    changeInterestRate.execute();
                    break;
                case "withdrawSavings":
                    WithdrawSavings withdrawSavings = new WithdrawSavings(graph, cmd, transactions,
                            users);
                    withdrawSavings.execute();
                    break;
                case "upgradePlan":
                    UpgradePlan upgradePlan = new UpgradePlan(graph, cmd, transactions, users);
                    upgradePlan.execute();
                    break;
                case "cashWithdrawal":
                    CashWithdrawal cashWithdrawal = new CashWithdrawal(users, commandNode, output,
                            cmd, objectMapper, transactions, graph);
                    cashWithdrawal.execute();
                    break;
                default:
                    break;
            }
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
}
