package org.poo.main.userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a card of an user.
 */
@Getter
@Setter
@NoArgsConstructor
public class Card {

    private String cardNumber;
    private String status;
    private int oneTime;
    private int frozen;

    /**
     * Constructs a new Card object with the specified details.
     *
     * @param cardNumber The card number.
     * @param status     The status of the card (e.g., "active", "frozen").
     * @param oneTime    Indicates if the card is one-time use (1 for true, 0 for false).
     */
    public Card(final String cardNumber, final String status, final int oneTime) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.oneTime = oneTime;
    }
}
