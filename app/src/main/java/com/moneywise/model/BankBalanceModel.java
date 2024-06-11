package com.moneywise.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BankBalanceModel implements Serializable, Parcelable {
    private final int id;
    private UserModel user;
    private BankModel bank;
    private double balance;

    public int getId() {
        return id;
    }

    public void subtractBal(double balance) {
        this.balance -= balance;
    }

    public void addBal(double balance) {
        this.balance += balance;
    }

    public BankModel getBank() {
        return bank;
    }

    public double getBalance() {
        return balance;
    }

    public BankBalanceModel(int id, UserModel user, BankModel bank, double balance) {
        this.id = id;
        this.user = user;
        this.bank = bank;
        this.balance = balance;
    }

    protected BankBalanceModel(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(UserModel.class.getClassLoader());
        bank = in.readParcelable(BankModel.class.getClassLoader());
        balance = in.readDouble();
    }

    public static final Creator<BankBalanceModel> CREATOR = new Creator<BankBalanceModel>() {
        @Override
        public BankBalanceModel createFromParcel(Parcel in) {
            return new BankBalanceModel(in);
        }

        @Override
        public BankBalanceModel[] newArray(int size) {
            return new BankBalanceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(bank, flags);
        dest.writeDouble(balance);
    }
}
