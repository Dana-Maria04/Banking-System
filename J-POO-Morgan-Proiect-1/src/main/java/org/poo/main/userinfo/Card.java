package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {

    private String cardNumber;
    private String status;

    public Card(String cardNumber, String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
