package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawSavingsTransaction extends Transaction{

    public WithdrawSavingsTransaction(final String description, final int timestamp, final String email) {
        super(description, timestamp, email, null);
    }

    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
    }
}
