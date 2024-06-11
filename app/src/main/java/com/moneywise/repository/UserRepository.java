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

        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_USER' untuk pengguna tertentu berdasarkan ID.
        // Query ini menggunakan klausa WHERE untuk memfilter hasil berdasarkan nilai 'id' yang diberikan.
        // Hasil query ini akan mengembalikan semua kolom dari tabel untuk pengguna dengan ID yang sesuai.
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
        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_USER' untuk pengguna tertentu berdasarkan email.
        // Query ini menggunakan klausa WHERE untuk memfilter hasil berdasarkan nilai 'email' yang diberikan.
        // Hasil query ini akan mengembalikan semua kolom dari tabel untuk pengguna dengan email yang sesuai.
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

        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_USER' untuk pengguna tertentu berdasarkan email dan password.
        // Query ini menggunakan klausa WHERE untuk memfilter hasil berdasarkan nilai 'email' dan 'password' yang diberikan.
        // Hasil query ini akan mengembalikan semua kolom dari tabel untuk pengguna dengan email dan password yang sesuai.
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
        // Membuat objek ContentValues untuk menyimpan pasangan kunci-nilai yang akan digunakan untuk memperbarui atau menyisipkan data ke dalam database.
        ContentValues cv = new ContentValues();

        // Menambahkan nilai untuk kolom 'email' dengan menggunakan nilai dari variabel 'email'.
        cv.put("email", email);

        // Menambahkan nilai untuk kolom 'password' dengan menggunakan nilai dari variabel 'password'.
        cv.put("password", password);
        long success = db.insert(Constant.TABLE_NAME_USER, null, cv);
        return success != -1;
    }

}
