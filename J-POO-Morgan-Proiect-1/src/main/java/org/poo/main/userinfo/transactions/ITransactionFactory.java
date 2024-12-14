package org.poo.main.userinfo.transactions;

import org.poo.main.userinfo.transactions.Transaction;

import java.util.Map;

public interface ITransactionFactory {
    Transaction createTransaction(String type, Map<String, Object> params);
}