package org.poo.main.userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.main.userinfo.transactions.Transaction;

import java.util.ArrayList;

/**
 * This class represents a user with associated accounts and transactions.
 * It stores the user's data and provides functionality to manage accounts and transactions.
 */
@Getter
@Setter
@NoArgsConstructor
public class User {

    private UserInput user;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;

    /**
     * Constructor to initialize the user with their data and associated accounts.
     *
     * @param user     The user input containing user details.
     * @param accounts A list of accounts associated with the user.
     */
    public User(final UserInput user, final ArrayList<Account> accounts) {
        this.user = user;
        this.accounts = accounts;
    }

    /**
     * Constructor to initialize the user with accounts.
     * If the accounts list is null, it initializes a new empty list.
     *
     * @param accounts A list of accounts associated with the user.
     */
    public User(final ArrayList<Account> accounts) {
        if (accounts != null) {
            this.accounts = accounts;
        } else {
            this.accounts = new ArrayList<>();
        }
    }
}
