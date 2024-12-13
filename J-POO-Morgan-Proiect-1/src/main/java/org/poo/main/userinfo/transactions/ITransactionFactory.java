package org.poo.main.userinfo.transactions;

import java.util.Map;

public interface ITransactionFactory {
    Transaction createTransaction(String type, Map<String, Object> params);
}