package com.moneywise.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.moneywise.constant.Constant;
import com.moneywise.helper.DBHelper;
import com.moneywise.model.BankBalanceModel;
import com.moneywise.model.BankModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BankRepository extends DBHelper implements IBankRepository {
    IUserRepository userRepository;

    public BankRepository(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        userRepository = new UserRepository(context, Constant.DB_NAME, null, Constant.VERSION);
    }

    @Override
    public BankModel getById(int id) {
        BankModel bankModel;

        String selectQuery = "SELECT * " +
                "FROM " + Constant.TABLE_NAME_BANK + " " +
                "WHERE id = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            bankModel = new BankModel(
                    cursor.getInt(0),
                    cursor.getString(1)
            );
        } else {
            bankModel = null;
        }

        cursor.close();
        db.close();

        return bankModel;
    }

    @Override
    public List<BankModel> getAll(int limit) {
        final ArrayList<BankModel> bankModel = new ArrayList<>();

        String selectQuery = "SELECT * " +
                "FROM " + Constant.TABLE_NAME_BANK + " " +
                "LIMIT " + limit;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                bankModel.add(new BankModel(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return bankModel;
    }

    @Override
    public boolean createAccount(int userId, int bankId, double initialBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
//        "user_id INT NOT NULL, " +
//        "bank_id INT NOT NULL, " +
//        "balance REAL NOT NULL DEFAULT 0, " +
//        "createdAt DATE DEFAULT CURRENT_DATE," +
//        "updatedAt DATE," +
        cv.put("user_id", userId);
        cv.put("bank_id", bankId);
        cv.put("balance", initialBalance);

        long success = db.insert(Constant.TABLE_NAME_BANK_BALANCE, null, cv);

        db.close();

        return success != -1;
    }

    @Override
    public List<BankBalanceModel> getAllBankBalance(int userId) {
        final ArrayList<BankBalanceModel> bankModel = new ArrayList<>();

        String selectQuery = "SELECT * " +
                "FROM " + Constant.TABLE_NAME_BANK_BALANCE + " " +
                "WHERE user_id = " + userId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                bankModel.add(new BankBalanceModel(
                        cursor.getInt(0),
                        userRepository.getById(cursor.getInt(1)),
                        getById(cursor.getInt(2)),
                        cursor.getDouble(3)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return bankModel;
    }

    @Override
    public double getTotalBalance(int userId) {
        double balance = 0;

        String selectQuery = "SELECT SUM(balance) " +
                "FROM " + Constant.TABLE_NAME_BANK_BALANCE + " " +
                "WHERE user_id = " + userId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return balance;
    }

    @Override
    public BankBalanceModel getBankBalanceByBankId(int userId, int bankId) {
        final BankBalanceModel bankModel;

        String selectQuery = "SELECT * " +
                "FROM " + Constant.TABLE_NAME_BANK_BALANCE + " " +
                "WHERE user_id = " + userId + " " +
                "AND bank_id = " + bankId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            bankModel = new BankBalanceModel(
            cursor.getInt(0),
            userRepository.getById(cursor.getInt(1)),
            getById(cursor.getInt(2)),
            cursor.getDouble(3));
        } else {
            bankModel = null;
        }

        cursor.close();
        db.close();

        return bankModel;
    }

    @Override
    public BankModel getByName(String name) {
        BankModel bankModel;

        String selectQuery = "SELECT * " +
                "FROM " + Constant.TABLE_NAME_BANK + " " +
                "WHERE name = '" + name + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            bankModel = new BankModel(
                    cursor.getInt(0),
                    cursor.getString(1)
            );
        } else {
            bankModel = null;
        }

        cursor.close();
        db.close();

        return bankModel;
    }

    @Override
    public boolean create(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);

        long success = db.insert(Constant.TABLE_NAME_BANK, null, cv);

        db.close();

        return success != -1;
    }

    @Override
    public boolean updateBalance(BankBalanceModel balanceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("balance", balanceModel.getBalance());

        int success = db.update(Constant.TABLE_NAME_BANK_BALANCE, cv, "id = " + balanceModel.getId(), null);

        db.close();

        return success != 0;
    }

    @Override
    public boolean delete(int bankId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete(Constant.TABLE_NAME_BANK, "id = " + bankId, null);
        db.close();
        return success != 0;
    }
}
