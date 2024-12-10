package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private String description;
    private int timestamp;
    private double amount;
    private String currency;
    private String receiverIban;
    private String senderIban;
    private String transferType;
    private String account;
    private String cardNumber;
    private String cardHolder;
    private double amountPayOnline;
    private String commerciant;

}
