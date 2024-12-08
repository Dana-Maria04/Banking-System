package org.poo.main.userinfo;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Getter
@Setter
public class User {

    private UserInput user;
    private ArrayList<Account> accounts;


    public User(UserInput user, ArrayList<Account> accounts) {
        this.user = user;
        this.accounts = accounts;
    }
    
    public User(ArrayList<Account> accounts){
        if(this.accounts != null){
            this.accounts = accounts;
        } else {
            this.accounts = new ArrayList<>();
        }
    }

    public UserInput getUser() {
        return user;
    }

    public void setUser(UserInput user) {
        this.user = user;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

}
