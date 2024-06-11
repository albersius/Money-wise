package com.moneywise.repository;

import com.moneywise.model.BankBalanceModel;
import com.moneywise.model.BankModel;

import java.util.List;

public interface IBankRepository {
    BankModel getById(int id);
    List<BankModel> getAll(int limit);
    boolean createAccount(int userId, int bankId, double initialBalance);
    List<BankBalanceModel> getAllBankBalance(int userId);
    double getTotalBalance(int userId);
    BankBalanceModel getBankBalanceByBankId(int userId, int bankId);
    BankModel getByName(String name);
    boolean create(String name);
    boolean updateBalance(BankBalanceModel balanceModel);
}
