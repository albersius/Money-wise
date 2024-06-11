package com.moneywise.model;

public class MonthlyTransactionModel {
    // yyyy-mm
    private String month;
    private double totalIncome;
    private double totalExpense;
    private double totalMonthly;

    public String getMonth() {
        return month;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public MonthlyTransactionModel(String month, double totalIncome, double totalExpense) {
        this.month = month + "/01";
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.totalMonthly = totalIncome - totalExpense;
    }

    public double getTotalMonthly() {
        return totalMonthly;
    }
}
