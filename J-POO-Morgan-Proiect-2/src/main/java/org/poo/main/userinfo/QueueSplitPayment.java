package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a queue for split payments, containing details such as involved accounts,
 * amounts, currency, and approval status.
 */
@Getter
@Setter
public class QueueSplitPayment {
    private final List<String> accounts;
    private final List<Double> amounts;
    private final double totalAmount;
    private final String currency;
    private final HashMap<String, Boolean> approvalStatus;
    private final int timestamp;
    private final String splitPaymentType;
    private boolean completed;

    /**
     * Constructs a QueueSplitPayment instance for a custom split payment.
     *
     * @param accounts        The list of IBANs involved in the payment.
     * @param amounts         The list of amounts corresponding to each account.
     * @param totalAmount     The total amount of the split payment.
     * @param currency        The currency of the payment.
     * @param approvalStatus  The approval status for each account.
     * @param timestamp       The timestamp of the payment.
     * @param splitPaymentType The type of split payment (e.g., "custom").
     */
    public QueueSplitPayment(final List<String> accounts, final List<Double> amounts,
                             final double totalAmount, final String currency,
                             final HashMap<String, Boolean> approvalStatus,
                             final int timestamp, final String splitPaymentType) {
        this.accounts = accounts;
        this.amounts = amounts;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.approvalStatus = approvalStatus;
        this.timestamp = timestamp;
        this.splitPaymentType = splitPaymentType;
        this.completed = false;
    }

    /**
     * Constructs a QueueSplitPayment instance for an equal split payment.
     *
     * @param accounts        The list of IBANs involved in the payment.
     * @param totalAmount     The total amount of the split payment.
     * @param currency        The currency of the payment.
     * @param approvalStatus  The approval status for each account.
     * @param timestamp       The timestamp of the payment.
     * @param splitPaymentType The type of split payment (e.g., "equal").
     */
    public QueueSplitPayment(final List<String> accounts, final double totalAmount,
                             final String currency,
                             final HashMap<String, Boolean> approvalStatus,
                             final int timestamp, final String splitPaymentType) {
        this.accounts = accounts;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.approvalStatus = approvalStatus;
        this.timestamp = timestamp;
        this.splitPaymentType = splitPaymentType;
        this.completed = false;
        this.amounts = new ArrayList<>();
    }

    /**
     * Marks the specified IBAN as having accepted the split payment.
     *
     * @param iban The IBAN of the account that has accepted the payment.
     */
    public void accept(final String iban) {
        approvalStatus.put(iban, true);
    }

    /**
     * Checks if all accounts have accepted the split payment.
     *
     * @return true if all accounts have accepted the payment; false otherwise.
     */
    public boolean isFullyAccepted() {
        return approvalStatus.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Marks the split payment as completed.
     */
    public void markAsCompleted() {
        this.completed = true;
    }
}
