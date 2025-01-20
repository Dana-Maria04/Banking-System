package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    //custom
    public QueueSplitPayment(List<String> accounts, List<Double> amounts, double totalAmount,
                               String currency, HashMap<String, Boolean> approvalStatus, int timestamp,
                               String splitPaymentType) {
        this.accounts = accounts;
        this.amounts = amounts;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.approvalStatus = approvalStatus;
        this.timestamp = timestamp;
        this.splitPaymentType = splitPaymentType;
        this.completed = false;
    }

    //equal
    public QueueSplitPayment(List<String> accounts, double totalAmount,
                               String currency, HashMap<String, Boolean> approvalStatus, int timestamp,
                               String splitPaymentType) {
        this.accounts = accounts;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.approvalStatus = approvalStatus;
        this.timestamp = timestamp;
        this.splitPaymentType = splitPaymentType;
        this.completed = false;
        this.amounts = new ArrayList<>();
    }

    public void accept(String iban) {
        approvalStatus.put(iban, true);
    }

    public boolean isFullyAccepted() {
        return approvalStatus.values().stream().allMatch(Boolean::booleanValue);
    }

    public void markAsCompleted() {
        this.completed = true;
    }
}