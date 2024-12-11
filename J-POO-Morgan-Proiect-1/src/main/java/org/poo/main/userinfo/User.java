package org.poo.main.userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private UserInput user;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;

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

}
