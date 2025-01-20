package org.poo.main.userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.main.userinfo.transactions.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private String userPlan;

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

    public int checkOldEnough() {
        if (user == null || user.getBirthDate() == null) {
            return 0;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(user.getBirthDate(), formatter);
        } catch (Exception e) {
            return 0;
        }

        long age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());

        return age >= 21 ? 1 : 0;
    }

    public String getFullNameFromEmail() {
        if (user == null || user.getEmail() == null) {
            return "";
        }

        String email = user.getEmail();
        String namePart = email.split("@")[0]; // Extract everything before '@'
        String[] nameParts = namePart.split("_");

        if (nameParts.length != 2) {
            return ""; // Return empty if format doesn't match expectations
        }

        String firstName = nameParts[0];
        String lastName = nameParts[1];

        return lastName + " " + firstName;
    }

}
