package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FrozenPayOnlineTransaction extends Transaction {

    public FrozenPayOnlineTransaction(String description, int timestamp,
                                      String email, String reportIban) {
        super(description, timestamp, email, reportIban);
    }

    @Override
    public void addDetailsToNode(ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }
}