package org.poo.main.userinfo.transactions.splitTransactions;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface SplitTransaction {
    void addSplitDetails(ObjectNode transactionNode);
}
