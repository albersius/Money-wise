package com.moneywise.repository;

import com.moneywise.model.TransactionModel;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

public interface ITransactionRepository {
    TransactionModel getById(int userId, int transactionId) throws Exception;
    List<TransactionModel> getAll(int userId, int limit) throws Exception;
    List<TransactionModel> getByDateRange(int userId, Date start, Date end) throws ParseException;
    boolean create(int userId, String label, double amount, String description, int bankId);
    void update(int userId, TransactionModel model);

    void delete(int userId, int id);
    double getBalance(int userId);
    double getBalanceByBankId(int userId, int bankId);
    double getIncome(int userId);
    double getIncomeByBankId(int userId, int bankId);
    double getExpense(int userId);
    double getExpenseByBankId(int userId, int bankId);
    double getExpenseByDate(int userId, Date date);
}
