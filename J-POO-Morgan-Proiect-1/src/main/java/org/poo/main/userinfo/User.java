package org.poo.main.userinfo;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

@Getter
@Setter
public class User {

    private UserInput user;
    private Account[] accounts;


    public User(UserInput user, Account[] accounts) {
        this.user = user;
        this.accounts = accounts;
    }
    
    public User(Account[] accounts){
        if(this.accounts != null){
            this.accounts = accounts;
        } else {
            this.accounts = new Account[0];
        }
    }

    public UserInput getUser() {
        return user;
    }

    public void setUser(UserInput user) {
        this.user = user;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

}
