package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

public class Cmmd {
    CommandHandler commandHandler;

    public ArrayNode execute(final ObjectInput objectInput) {
        for(CommandInput cmd : objectInput.getCommands()) {
            switch(cmd.getCommand()) {
                case "printUsers":
//                    commandHandler.printUsers();
                    break;
                case "remove":
                    break;
                case "update":
                    break;
                case "list":
                    break;
                default:
                    break;

            }
        }

        return null;
    }
}
