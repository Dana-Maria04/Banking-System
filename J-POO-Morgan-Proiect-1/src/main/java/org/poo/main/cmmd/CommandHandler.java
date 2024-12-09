package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.userinfo.Account;
import org.poo.main.userinfo.ExchangeGraph;
import org.poo.main.userinfo.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class CommandHandler {

    private ArrayList<User> users;
    private ArrayList<CommandInput> commands;
    private ArrayList<ExchangeInput> exchangeRates;
    private ExchangeGraph graph;

    public ArrayNode handle(final ObjectInput objectInput, ArrayNode output) {
        users = new ArrayList<>();

        for (UserInput userInput : objectInput.getUsers()) {
            User user = new User(new ArrayList<>());
            user.setUser(userInput);
            users.add(user);
        }

        exchangeRates = new ArrayList<>();

        for(ExchangeInput exchangeInput : objectInput.getExchangeRates()){
            ExchangeInput exchange = new ExchangeInput();
            exchange.setTo(exchangeInput.getTo());
            exchange.setRate(exchangeInput.getRate());
            exchange.setFrom(exchangeInput.getFrom());
            exchange.setTimestamp(exchangeInput.getTimestamp());
            exchangeRates.add(exchange);

        }

        graph = new ExchangeGraph(exchangeRates);

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
                    CreateCard createCard = new CreateCard(users, cmd, 0);
                    createCard.execute();
                    break;
                case "addAccount":
                    AddAccount addAccount = new AddAccount(users, commandNode, output, cmd, objectMapper);
                    addAccount.execute();
                    break;
                case "deleteAccount":
                    DeleteAccount deleteAccount = new DeleteAccount(users, commandNode, output, cmd, objectMapper);
                    deleteAccount.execute();
                    break;
                case "createOneTimeCard":
                    CreateCard createOneTimeCard = new CreateCard(users, cmd, 1);
                    createOneTimeCard.execute();
                    break;
                case "deleteCard":
                    DeleteCard deleteCard = new DeleteCard(users, cmd);
                    deleteCard.execute();
                    break;
                case "setMinimumBalanmce":
                    SetMinimumBalance setMinimumBalance = new SetMinimumBalance(users, cmd);
                    setMinimumBalance.execute();
                    break;
                case "payOnline":
                    PayOnline payOnline = new PayOnline(users, cmd, graph, output, objectMapper, commandNode);
                    payOnline.execute();
                    break;
                case "sendMoney":
                    SendMoney sendMoney = new SendMoney(users, cmd, graph, output, objectMapper, commandNode);
                    sendMoney.execute();
                    break;
                default:
                    break;
            }
        }

        return output;
    }
}
