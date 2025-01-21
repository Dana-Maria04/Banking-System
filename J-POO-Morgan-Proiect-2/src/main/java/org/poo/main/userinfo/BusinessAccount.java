package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

/**
 * Represents a BusinessAccount that extends the Account class with additional features.
 */
@Getter
@Setter
public class BusinessAccount extends Account {
    private String alias;
    private int numberOfTransactions;
    private int gotFoodCashback;
    private int gotClothesCashback;
    private int gotTechCashback;
    private int gotCashbacks;
    private String accountPlan;
    private int paymentsOver300;
    private int foodPayments;
    private int clothesPayments;
    private int techPayments;
    private double deposited;
    private double spent;
    private String ownerEmail;
    private ArrayList<User> employees;
    private ArrayList<User> managers;
    private ArrayList<Card> cards;
    private double spendingLimit;
    private double depositLimit;

    /**
     * Constructor for BusinessAccount.
     *
     * @param iban             The IBAN of the account.
     * @param accountType      The type of the account.
     * @param currency         The currency of the account.
     * @param balance          The balance of the account.
     * @param minimumBalance   The minimum balance of the account.
     * @param interestRate     The interest rate of the account.
     */
    public BusinessAccount(final String iban, final String accountType,
                           final String currency, final double balance,
                           final double minimumBalance, final double interestRate,
                           final String ownerEmail) {
        super(iban, accountType, currency, balance, new ArrayList<>());
        this.setMinimumBalance(minimumBalance);
        this.setInterestRate(interestRate);

        this.ownerEmail = ownerEmail;

        this.numberOfTransactions = 0;
        this.gotFoodCashback = 0;
        this.gotClothesCashback = 0;
        this.gotTechCashback = 0;
        this.gotCashbacks = 0;

        this.paymentsOver300 = 0;
        this.foodPayments = 0;
        this.clothesPayments = 0;
        this.techPayments = 0;

        this.deposited = 0;
        this.spent = 0;

        this.employees = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    /**
     * Adds an employee to the business account.
     *
     * @param employee The employee to add.
     */
    public void addEmployee(final User employee) {
        this.employees.add(employee);
    }

    /**
     * Adds a manager to the business account.
     *
     * @param manager The manager to add.
     */
    public void addManager(final User manager) {
        this.managers.add(manager);
    }

    /**
     * Records a payment made by the business account.
     *
     * @param amount The payment amount.
     * @param category The category of the payment (e.g., "food", "clothes", "tech").
     */
    public void recordPayment(final double amount, final String category) {
        this.spent += amount;
        this.numberOfTransactions++;

        if (amount > 300) {
            this.paymentsOver300++;
        }

        switch (category.toLowerCase()) {
            case "food" -> this.foodPayments++;
            case "clothes" -> this.clothesPayments++;
            case "tech" -> this.techPayments++;
            default -> {
                System.out.printf("Invalid category: %s%n", category);
            }
        }
    }

    /**
     * Records a deposit made to the business account.
     *
     * @param amount The deposit amount.
     */
    public void recordDeposit(final double amount) {
        this.deposited += amount;
        this.setBalance(this.getBalance() + amount);
    }
}
