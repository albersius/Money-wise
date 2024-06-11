package com.moneywise.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.moneywise.constant.Constant;
import com.moneywise.helper.DBHelper;
import com.moneywise.model.BankModel;
import com.moneywise.model.MonthlyTransactionModel;
import com.moneywise.model.TransactionModel;
import com.moneywise.model.UserModel;

import java.text.ParseException;
import java.sql.Date;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository extends DBHelper implements ITransactionRepository {


    public TransactionRepository(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public TransactionModel getById(int userId, int transactionId)  {
        TransactionModel transactionModel;

        String selectStatement = "SELECT " +
                "    t.id AS transaction_id," +
                "    t.label AS transaction_label," +
                "    t.amount AS transaction_amount," +
                "    t.description AS transaction_description," +
                "    t.createdAt AS transaction_created_at," +
                "    u.id AS user_id," +
                "    u.email AS user_email," +
                "    u.createdAt AS user_created_at," +
                "    b.id AS bank_id," +
                "    b.name AS bank_name " +
                "FROM " +
                "    transactions t " +
                "JOIN " +
                "    users u ON t.user_id = u.id " +
                "JOIN " +
                "    banks b ON t.bank_id = b.id " +
                "WHERE " +
                "    t.id = " + transactionId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()) {
            transactionModel = newTModelFromCursor(cursor);
        } else {
            cursor.close();
            transactionModel = null;
        }

        cursor.close();

        return transactionModel;
    }

    @Override
    public List<TransactionModel> getAll(int userId, int limit) {
        final ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();

        String selectStatement = "SELECT " +
                "    t.id AS transaction_id," +
                "    t.label AS transaction_label," +
                "    t.amount AS transaction_amount," +
                "    t.description AS transaction_description," +
                "    t.createdAt AS transaction_created_at," +
                "    u.id AS user_id," +
                "    u.email AS user_email," +
                "    u.createdAt AS user_created_at," +
                "    b.id AS bank_id," +
                "    b.name AS bank_name " +
                "FROM " +
                "    transactions t " +
                "JOIN " +
                "    users u ON t.user_id = u.id " +
                "JOIN " +
                "    banks b ON t.bank_id = b.id " +
                "LIMIT " + limit;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()) {
            do {
                transactionModelArrayList.add(newTModelFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return transactionModelArrayList;
    }

    @Override
    public List<MonthlyTransactionModel> getMonthlyByYear(int userId, int year) {
        final ArrayList<MonthlyTransactionModel> models = new ArrayList<>();

        String selectStatement = "SELECT " +
                "    strftime('%Y/%m', t.createdAt) AS month," +
                "    SUM(CASE WHEN t.label = 'label_income' THEN t.amount ELSE 0 END) AS total_income," +
                "    SUM(CASE WHEN t.label = 'label_expense' THEN ABS(t.amount) ELSE 0 END) AS total_expense " +
                "FROM " +
                "   transactions t " +
                "WHERE " +
                "    strftime('%Y-%m', t.createdAt) >= '" + year + "-01' AND " +
                "    strftime('%Y-%m', t.createdAt) <= '" + year + "-12' AND " +
                "    user_id = '" + userId + "' " +
                "GROUP BY " +
                "    month";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()) {
            do {
                models.add(new MonthlyTransactionModel(
                        cursor.getString(0),
                        cursor.getDouble(1),
                        cursor.getDouble(2)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return models;
    }

    @Override
    public List<TransactionModel> getByDateRange(int userId, Date start, Date end) throws ParseException {
        List<TransactionModel> models = new ArrayList<>();

        String selectStatement = "SELECT \n" +
                "    t.id AS transaction_id,\n" +
                "    t.label AS transaction_label,\n" +
                "    t.amount AS transaction_amount,\n" +
                "    t.description AS transaction_description,\n" +
                "    t.createdAt AS transaction_created_at,\n" +
                "    u.id AS user_id,\n" +
                "    u.email AS user_email,\n" +
                "    u.createdAt AS user_created_at,\n" +
                "    b.id AS bank_id,\n" +
                "    b.name AS bank_name\n" +
                "FROM \n" +
                "    transactions t\n" +
                "JOIN \n" +
                "    users u ON t.user_id = u.id\n" +
                "JOIN \n" +
                "    banks b ON t.bank_id = b.id\n" +
                "WHERE \n" +
                "    t.createdAt >= " + start.toString() +
                " AND t.createdAt <= " + end.toString();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, new String[]{start.toString(), end.toString()});
        if (cursor.moveToFirst()) {
            do {
                models.add(newTModelFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return models;
    }

    @Override
    public boolean create(int userId, String label, double amount, String description, int bankId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("label", label);
        cv.put("amount", amount);
        cv.put("description", description);
        cv.put("user_id", userId);
        cv.put("bank_id", bankId);

        long success = db.insert(Constant.TABLE_NAME_TRANSACTION, null, cv);
        return success != -1;
    }

    @Override
    public void update(int userId, TransactionModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("label", model.getLabel());
        cv.put("amount", model.getAmount());
        cv.put("description", model.getDescription());
        cv.put("user_id", model.getUser().getId());
        cv.put("bank_id", model.getBank().getId());

        db.update(Constant.TABLE_NAME_TRANSACTION, cv, "user_id = " + model.getId(), null);
    }

    @Override
    public void delete(int userId, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constant.TABLE_NAME_TRANSACTION, "user_id = " + id, null);
    }

    @Override
    public double getBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(amount) AS balance " +
                "FROM " + Constant.TABLE_NAME_TRANSACTION + " " +
                "WHERE user_id = " + userId;
        Cursor cursor = db.rawQuery(selectQuery, null);

        double balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        } else {
            balance = 0;
        }

        cursor.close();

        return balance;
    }

    @Override
    public double getBalanceByBankId(int userId, int bankId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(amount) AS balance " +
                "FROM " + Constant.TABLE_NAME_TRANSACTION + " " +
                "WHERE user_id = " + userId + " AND bank_id = " + bankId;
        Cursor cursor = db.rawQuery(selectQuery, null);

        double balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        } else {
            balance = 0;
        }

        cursor.close();

        return balance;
    }

    @Override
    public double getIncome(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(amount) AS balance " +
                "FROM " + Constant.TABLE_NAME_TRANSACTION + " " +
                "WHERE user_id = " + userId + " AND label = '" + Constant.LABEL_INCOME + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        double balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        } else {
            balance = 0;
        }

        cursor.close();

        return balance;
    }

    @Override
    public double getIncomeByBankId(int userId, int bankId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(amount) AS balance " +
                "FROM " + Constant.TABLE_NAME_TRANSACTION + " " +
                "WHERE user_id = " + userId +
                "   AND label = '" + Constant.LABEL_INCOME + "' " +
                "   AND bank_id = " + bankId;
        Cursor cursor = db.rawQuery(selectQuery, null);

        double balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        } else {
            balance = 0;
        }

        cursor.close();

        return balance;
    }

    @Override
    public double getExpense(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(amount) AS balance " +
                "FROM " + Constant.TABLE_NAME_TRANSACTION + " " +
                "WHERE user_id = " + userId + " AND label = '" + Constant.LABEL_EXPENSE + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        double balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        } else {
            balance = 0;
        }

        cursor.close();

        return balance;
    }

    @Override
    public double getExpenseByBankId(int userId, int bankId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(amount) AS balance " +
                "FROM " + Constant.TABLE_NAME_TRANSACTION + " " +
                "WHERE user_id = " + userId +
                "   AND label = '" + Constant.LABEL_EXPENSE + "'" +
                "   AND bank_id = " + bankId;
        Cursor cursor = db.rawQuery(selectQuery, null);

        double balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        } else {
            balance = 0;
        }

        cursor.close();

        return balance;
    }

    @Override
    public double getExpenseByDate(int userId, Date date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(amount) AS balance " +
                "FROM " + Constant.TABLE_NAME_TRANSACTION + " " +
                "WHERE user_id = " + userId +
                "   AND label = '" + Constant.LABEL_EXPENSE + "'" +
                "   AND createdAt = " + date.toString();
        Cursor cursor = db.rawQuery(selectQuery, null);

        double balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        } else {
            balance = 0;
        }

        cursor.close();

        return balance;
    }

    private TransactionModel newTModelFromCursor(Cursor cursor) {
        //            transaction_id	0
        //            transaction_label	1
        //            transaction_amount	2
        //            transaction_description	3
        //            transaction_created_at	4
        //            user_id	5
        //            user_email	6
        //            user_created_at	7
        //            bank_id	8
        //            bank_name  9

        UserModel userModel;
        BankModel bankModel;
        userModel = new UserModel(
                cursor.getInt(5),
                cursor.getString(6)
        );
        bankModel = new BankModel(
                cursor.getInt(8),
                cursor.getString(9)
//                Date.valueOf(cursor.getString(9))
        );
        return new TransactionModel(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getString(3),
                Date.valueOf(cursor.getString(4)),
                userModel,
                bankModel
        );
    }
}
