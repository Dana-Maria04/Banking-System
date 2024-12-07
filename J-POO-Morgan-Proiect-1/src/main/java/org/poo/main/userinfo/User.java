package org.poo.main.userinfo;

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
    
    public void newAccount(Account[] accounts){
        if(this.accounts != null){
            this.accounts = accounts;
        } else {
            accounts = new Account[0];
        }
    }
}
