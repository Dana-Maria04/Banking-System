package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

@Setter
@Getter
public class Account {
//    private String iban;
//    private double balance;
//    private String currency;

    private Card[] cards;
    private CommandInput accountInfo;

    public Account(Card[] cards, CommandInput accountInfo) {
        this.cards = cards;
        this.accountInfo = accountInfo;
    }
}
