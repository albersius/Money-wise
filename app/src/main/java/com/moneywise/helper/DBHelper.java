package com.moneywise.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.moneywise.constant.Constant;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(
            @Nullable Context context,
            @Nullable String name,
            @Nullable SQLiteDatabase.CursorFactory factory,
            int version
    ) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create user table
        String createTableStatement = "CREATE TABLE " +
                Constant.TABLE_NAME_USER +
                " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email VARCHAR(255) NOT NULL UNIQUE, " +
                "password VARCHAR(50) NOT NULL, " +
                "createdAt DATE DEFAULT CURRENT_DATE" +
                ")";

        db.execSQL(createTableStatement);

        // create bank table
        createTableStatement = "CREATE TABLE " +
                Constant.TABLE_NAME_BANK +
                " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL " +
                ")";

        db.execSQL(createTableStatement);

        // create bank_balance table
        createTableStatement = "CREATE TABLE " +
                Constant.TABLE_NAME_BANK_BALANCE +
                " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INT NOT NULL, " +
                "bank_id INT NOT NULL, " +
                "balance REAL NOT NULL DEFAULT 0, " +
                "createdAt DATE DEFAULT CURRENT_DATE," +
                "updatedAt DATE," +
                "FOREIGN KEY(user_id) REFERENCES " + Constant.TABLE_NAME_USER + "(id) " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(bank_id) REFERENCES " + Constant.TABLE_NAME_BANK + "(id)" +
                "ON UPDATE CASCADE ON DELETE CASCADE " +
                ")";

        db.execSQL(createTableStatement);

        // create transaction table
        createTableStatement = "CREATE TABLE " +
                Constant.TABLE_NAME_TRANSACTION +
                " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "label VARCHAR(255) NOT NULL, " +
                "amount REAL NOT NULL DEFAULT 0, " +
                "description TEXT, " +
                "createdAt DATE DEFAULT CURRENT_DATE," +
                "user_id INT NOT NULL, " +
                "bank_id INT NOT NULL, " +
                "FOREIGN KEY(user_id) REFERENCES " + Constant.TABLE_NAME_USER + "(id) " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(bank_id) REFERENCES " + Constant.TABLE_NAME_BANK + "(id)" +
                "ON UPDATE CASCADE ON DELETE CASCADE " +
                ")";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
