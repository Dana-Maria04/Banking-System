package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.main.userinfo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class CommandHandler {

    private ArrayList<User> users;
    private ArrayList<CommandInput> commands;
    private ArrayList<ExchangeInput> exchangeRates;
    private ExchangeGraph graph;
    private ArrayList<Transaction> transactions;

    public ArrayNode handle(final ObjectInput objectInput, ArrayNode output) {
        users = new ArrayList<>();

        for (UserInput userInput : objectInput.getUsers()) {
            User user = new User(new ArrayList<>());
            user.setUser(userInput);

            user.setTransactions(new ArrayList<>());

            users.add(user);
        }

        exchangeRates = new ArrayList<>();

        for(ExchangeInput exchangeInput : objectInput.getExchangeRates()){
            // constructor for ExchangeInput
            ExchangeInput exchange = new ExchangeInput();
            exchange.setTo(exchangeInput.getTo());
            exchange.setRate(exchangeInput.getRate());
            exchange.setFrom(exchangeInput.getFrom());
            exchange.setTimestamp(exchangeInput.getTimestamp());
            exchangeRates.add(exchange);

        }

        graph = new ExchangeGraph(exchangeRates);

        transactions = new ArrayList<>();

        commands = new ArrayList<>(objectInput.getCommands().length);
        for (CommandInput commandInput : objectInput.getCommands()) {
            commands.add(commandInput);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        Utils.resetRandom();

        for (CommandInput cmd : commands) {
            ObjectNode commandNode = objectMapper.createObjectNode();

            switch (cmd.getCommand()) {
                case "printUsers":
                    PrintUsers printUsers = new PrintUsers(users, commandNode, output, cmd, objectMapper);
                    printUsers.execute();
                    break;
                case "addFunds":
                    AddFunds addFunds = new AddFunds(users, commandNode, output, cmd, objectMapper);
                    addFunds.execute();
                    break;
                case "createCard":
                    CreateCard createCard = new CreateCard(users, cmd, 0, transactions);
                    createCard.execute();
                    break;
                case "addAccount":
                    AddAccount addAccount = new AddAccount(users, commandNode, output, cmd, objectMapper, transactions);
                    addAccount.execute();
                    break;
                case "deleteAccount":
                    DeleteAccount deleteAccount = new DeleteAccount(users, commandNode, output, cmd, objectMapper, transactions);
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
                    PayOnline payOnline = new PayOnline(users, cmd, graph, output, objectMapper, commandNode, transactions);
                    payOnline.execute();
                    break;
                case "sendMoney":
                    SendMoney sendMoney = new SendMoney(users, cmd, graph, output, objectMapper, commandNode, transactions);
                    sendMoney.execute();
                    break;
                case "printTransactions":
                    PrintTransactions printTransactions = new PrintTransactions(users, commandNode, output, cmd, objectMapper, transactions);
                    printTransactions.execute();
                    break;
                case "checkCardStatus":
                    CheckCardStatus checkCardStatus = new CheckCardStatus(users, commandNode, output, cmd, objectMapper, transactions);
                    checkCardStatus.execute();
                    break;
                case "splitPayment":
                    SplitPayment splitPayment = new SplitPayment(graph, users, commandNode, output, cmd, objectMapper, transactions);
                    splitPayment.execute();
                    break;
                case "report":
                    Report report = new Report(users, cmd, graph, output, objectMapper, commandNode, transactions);
                    report.execute();
                    break;
                default:
                    break;
            }
        }

        return output;
    }
}