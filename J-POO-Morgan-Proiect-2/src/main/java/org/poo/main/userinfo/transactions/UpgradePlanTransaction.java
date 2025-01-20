package org.poo.main.userinfo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpgradePlanTransaction extends Transaction {
    private final String iban;
    private final String newPlanType;

    public UpgradePlanTransaction(final String description, final int timestamp,
                                  final String email, final String iban, final String newPlanType) {
        super(description, timestamp, email, null);
        this.iban = iban;
        this.newPlanType = newPlanType;
    }

    @Override
    public void addDetailsToNode(final ObjectNode transactionNode) {
        transactionNode.put("description", getDescription());
        transactionNode.put("timestamp", getTimestamp());
//        transactionNode.put("email", getEmail());
        transactionNode.put("accountIBAN", iban);
        transactionNode.put("newPlanType", newPlanType);
    }
}
