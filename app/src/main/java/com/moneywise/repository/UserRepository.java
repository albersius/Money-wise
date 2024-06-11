package com.moneywise.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.moneywise.constant.Constant;
import com.moneywise.helper.DBHelper;
import com.moneywise.model.UserModel;

public class UserRepository extends DBHelper implements IUserRepository {
    public UserRepository(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public UserModel getById(int id) {
        UserModel model;
        String selectStatement = "SELECT * FROM " +
                Constant.TABLE_NAME_USER +
                " WHERE id = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()) {
            int selectedId = cursor.getInt(0);
            String selectedEmail = cursor.getString(1);
            model = new UserModel(selectedId, selectedEmail);
        } else {
            model = null;
        }

        cursor.close();

        return model;
    }

    @Override
    public boolean isExistByEmail(String email) {
        String selectStatement = "SELECT * FROM " +
                Constant.TABLE_NAME_USER + " WHERE email = '" + email + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        boolean exist = cursor.moveToFirst();

        cursor.close();

        return exist;
    }

    @Override
    public UserModel getByEmailPassword(String email, String password) throws Exception {
        UserModel model;
        String selectStatement = "SELECT * FROM " +
                Constant.TABLE_NAME_USER +
                " WHERE email = '" + email + "' AND password = '" + password + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()) {
            int selectedId = cursor.getInt(0);
            String selectedEmail = cursor.getString(1);
            model = new UserModel(selectedId, selectedEmail);
        } else {
            model = null;
        }

        cursor.close();

        return model;
    }

    @Override
    public boolean create(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        long success = db.insert(Constant.TABLE_NAME_USER, null, cv);
        return success != -1;
    }

}
