package com.moneywise.repository;

import com.moneywise.model.MonthlyTransactionModel;
import com.moneywise.model.TransactionModel;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

public interface ITransactionRepository {
    List<TransactionModel> getAll(int userId, int limit);
    List<TransactionModel> getAllNote(int userId, int limit);
    List<MonthlyTransactionModel> getMonthlyByYear(int userId, int year);

    boolean create(int userId, String label, double amount, String description, int bankId);

    double getTotalNote(int userId);
    double getBalance(int userId);

    double getIncome(int userId);

    double getExpense(int userId);
}
