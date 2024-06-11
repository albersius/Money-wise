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

        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_BANK' untuk bank tertentu berdasarkan ID.
        // Query ini menggunakan klausa WHERE untuk memfilter hasil berdasarkan nilai 'id' yang diberikan.
        // Hasil query ini akan mengembalikan semua kolom dari tabel untuk bank dengan ID yang sesuai.
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

        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_BANK' dengan batasan jumlah hasil yang dikembalikan oleh 'LIMIT'.
        // Hasil query ini akan mengembalikan semua kolom dari tabel bank, namun dibatasi dengan jumlah maksimum yang ditentukan oleh variabel 'limit'.
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
        // Membuat objek ContentValues untuk menyimpan pasangan kunci-nilai yang akan digunakan untuk memasukkan data ke dalam database.
        ContentValues cv = new ContentValues();

        // Menambahkan nilai untuk kolom 'user_id' dengan menggunakan nilai dari variabel 'userId'.
        cv.put("user_id", userId);

        // Menambahkan nilai untuk kolom 'bank_id' dengan menggunakan nilai dari variabel 'bankId'.
        cv.put("bank_id", bankId);

        // Menambahkan nilai untuk kolom 'balance' dengan menggunakan nilai dari variabel 'initialBalance'.
        cv.put("balance", initialBalance);

        long success = db.insert(Constant.TABLE_NAME_BANK_BALANCE, null, cv);

        db.close();

        return success != -1;
    }

    @Override
    public List<BankBalanceModel> getAllBankBalance(int userId) {
        final ArrayList<BankBalanceModel> bankModel = new ArrayList<>();

        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_BANK_BALANCE' untuk saldo bank tertentu berdasarkan user_id.
        // Query ini menggunakan klausa WHERE untuk memfilter hasil berdasarkan nilai 'user_id' yang diberikan.
        // Hasil query ini akan mengembalikan semua kolom dari tabel saldo bank untuk pengguna dengan user_id yang sesuai.
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

        // Query ini menghitung total saldo (balance) dari semua catatan saldo bank untuk pengguna tertentu berdasarkan user_id.
        // Query ini menggunakan fungsi SUM untuk menjumlahkan semua nilai kolom 'balance'.
        // Hasil query ini akan mengembalikan jumlah total saldo dari semua catatan saldo bank untuk pengguna dengan user_id yang sesuai.
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

        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_BANK_BALANCE' untuk saldo bank tertentu berdasarkan user_id dan bank_id.
        // Query ini menggunakan klausa WHERE untuk memfilter hasil berdasarkan nilai 'user_id' dan 'bank_id' yang diberikan.
        // Hasil query ini akan mengembalikan semua kolom dari tabel saldo bank untuk pengguna dengan user_id dan bank_id yang sesuai.
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

        // Query ini mengambil semua data dari tabel 'Constant.TABLE_NAME_BANK' untuk bank tertentu berdasarkan nama bank.
        // Query ini menggunakan klausa WHERE untuk memfilter hasil berdasarkan nilai 'name' yang diberikan.
        // Hasil query ini akan mengembalikan semua kolom dari tabel bank untuk bank dengan nama yang sesuai.
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

        // Membuat objek ContentValues untuk menyimpan pasangan kunci-nilai yang akan digunakan untuk memperbarui atau menyisipkan data ke dalam database.
        ContentValues cv = new ContentValues();

        // Menambahkan nilai untuk kolom 'name' dengan menggunakan nilai dari variabel 'name'.
        cv.put("name", name);

        long success = db.insert(Constant.TABLE_NAME_BANK, null, cv);

        db.close();

        return success != -1;
    }

    @Override
    public boolean updateBalance(BankBalanceModel balanceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Membuat objek ContentValues untuk menyimpan pasangan kunci-nilai yang akan digunakan untuk memperbarui atau menyisipkan data ke dalam database.
        ContentValues cv = new ContentValues();

        // Menambahkan nilai untuk kolom 'balance' dengan menggunakan nilai dari objek BalanceModel yang dimasukkan.
        cv.put("balance", balanceModel.getBalance());

        int success = db.update(Constant.TABLE_NAME_BANK_BALANCE, cv, "id = " + balanceModel.getId(), null);

        db.close();

        return success != 0;
    }

}
