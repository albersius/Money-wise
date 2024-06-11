package com.moneywise.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.moneywise.constant.Constant;
import com.moneywise.helper.DBHelper;
import com.moneywise.model.BankModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BankRepository extends DBHelper implements IBankRepository {
    public BankRepository(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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
    public boolean update(int bankId, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);

        int success = db.update(Constant.TABLE_NAME_BANK, cv, "id = " + bankId, null);

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