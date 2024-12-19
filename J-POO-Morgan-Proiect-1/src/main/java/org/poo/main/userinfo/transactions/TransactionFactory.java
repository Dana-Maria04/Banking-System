package org.poo.main.userinfo.transactions;

import java.util.Map;

public interface TransactionFactory {
    /**
     * Create a transaction based on the given type and parameters.
     */
    Transaction createTransaction(String type, Map<String, Object> params);
}
