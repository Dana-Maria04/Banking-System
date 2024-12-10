package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {

    private String cardNumber;
    private String status;
    private int oneTime;
    private int frozen;

    public Card(String cardNumber, String status, int oneTime) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.oneTime = oneTime;
    }

}
