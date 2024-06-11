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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository extends DBHelper implements ITransactionRepository {


    public TransactionRepository(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public List<TransactionModel> getAll(int userId, int limit) {
        final ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();

        // Query ini mengambil data transaksi, pengguna, dan bank terkait dari tabel transactions, users, dan banks.
        // Kolom yang diambil dari tabel transactions adalah id, label, amount, description, dan createdAt.
        // Kolom yang diambil dari tabel users adalah id, email, dan createdAt.
        // Kolom yang diambil dari tabel banks adalah id dan name.
        // Query ini menggunakan JOIN untuk menghubungkan tabel transactions dengan users berdasarkan user_id,
        // serta menghubungkan tabel transactions dengan banks berdasarkan bank_id.
        // Data yang dihasilkan dibatasi dengan jumlah yang ditentukan oleh variabel 'limit'.
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
    public List<TransactionModel> getAllNote(int userId, int limit) {
        final ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();

        // query ini akan mengambil semua data note
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
                "WHERE t.label = '" + Constant.LABEL_NOTE + "' " +
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

        // Query ini mengambil data total pemasukan (income) dan pengeluaran (expense) per bulan untuk pengguna tertentu pada tahun tertentu.
        // Query menggunakan fungsi strftime untuk memformat kolom createdAt menjadi format 'YYYY/MM' untuk grouping dan 'YYYY-MM' untuk filtering.
        // Query menjumlahkan nilai amount dengan kondisi label 'label_income' sebagai total_income dan label 'label_expense' sebagai total_expense.
        // Data difilter berdasarkan tahun tertentu dan user_id tertentu, kemudian dikelompokkan per bulan.
        // Parameter yang digunakan adalah 'year' untuk tahun, dan 'userId' untuk ID pengguna.
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
    public boolean create(int userId, String label, double amount, String description, int bankId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Membuat objek ContentValues untuk menyimpan pasangan kunci-nilai yang akan digunakan untuk memasukkan data ke dalam database.
        ContentValues cv = new ContentValues();

        // Menambahkan nilai untuk kolom 'label' dengan menggunakan nilai dari variabel 'label'.
        cv.put("label", label);

        // Menambahkan nilai untuk kolom 'amount' dengan menggunakan nilai dari variabel 'amount'.
        cv.put("amount", amount);

        // Menambahkan nilai untuk kolom 'description' dengan menggunakan nilai dari variabel 'description'.
        cv.put("description", description);

        // Menambahkan nilai untuk kolom 'user_id' dengan menggunakan nilai dari variabel 'userId'.
        cv.put("user_id", userId);

        // Menambahkan nilai untuk kolom 'bank_id' dengan menggunakan nilai dari variabel 'bankId'.
        cv.put("bank_id", bankId);

        long success = db.insert(Constant.TABLE_NAME_TRANSACTION, null, cv);
        return success != -1;
    }

    @Override
    public double getTotalNote(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query ini menghitung total saldo (balance) untuk pengguna tertentu berdasarkan label 'note'.
        // Query menjumlahkan nilai amount hanya jika label transaksi adalah 'Constant.LABEL_NOTE'.
        // Hasilnya akan disimpan dalam alias 'balance'.
        // Query ini mengambil data dari tabel 'Constant.TABLE_NAME_TRANSACTION' dan memfilter berdasarkan user_id tertentu.
        String selectQuery = "SELECT " +
                "SUM(CASE WHEN label = '"+ Constant.LABEL_NOTE +"' THEN amount ELSE 0 END) " +
                "AS balance " +
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
    public double getBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query ini menghitung saldo (balance) untuk pengguna tertentu dengan mengurangkan total pengeluaran (expense) dari total pemasukan (income).
        // Query menjumlahkan nilai amount untuk transaksi dengan label 'Constant.LABEL_INCOME' sebagai total pemasukan.
        // Query menjumlahkan nilai amount untuk transaksi dengan label 'Constant.LABEL_EXPENSE' sebagai total pengeluaran.
        // Hasil pengurangan total pemasukan dengan total pengeluaran disimpan dalam alias 'balance'.
        // Query ini mengambil data dari tabel 'Constant.TABLE_NAME_TRANSACTION' dan memfilter berdasarkan user_id tertentu.
        String selectQuery = "SELECT " +
                "(SUM(CASE WHEN label = '"+ Constant.LABEL_INCOME +"' THEN amount ELSE 0 END) - " +
                "SUM(CASE WHEN label = '" + Constant.LABEL_EXPENSE + "' THEN amount ELSE 0 END)) " +
                "AS balance " +
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
    public double getIncome(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query ini menghitung total saldo (balance) dari semua pemasukan (income) untuk pengguna tertentu.
        // Query menjumlahkan nilai amount dari transaksi yang memiliki label 'Constant.LABEL_INCOME'.
        // Hasilnya disimpan dalam alias 'balance'.
        // Query ini mengambil data dari tabel 'Constant.TABLE_NAME_TRANSACTION' dan memfilter berdasarkan user_id dan label income.
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
    public double getExpense(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query ini menghitung total saldo (balance) dari semua pengeluaran (expense) untuk pengguna tertentu.
        // Query menjumlahkan nilai amount dari transaksi yang memiliki label 'Constant.LABEL_EXPENSE'.
        // Hasilnya disimpan dalam alias 'balance'.
        // Query ini mengambil data dari tabel 'Constant.TABLE_NAME_TRANSACTION' dan memfilter berdasarkan user_id dan label expense.
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

    // sebuah method untuk mengubah cursor menjadi model transaksi
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
