package org.poo.main.cmmd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.userinfo.User;

public interface Command {
    User[] execute();
    void undo();
}
