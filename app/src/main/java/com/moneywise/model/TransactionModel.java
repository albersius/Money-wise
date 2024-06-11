package com.moneywise.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class TransactionModel implements Parcelable, Serializable
{
    private int id;
    private final String label;
    private final double amount;
    private String description;
    private Date createdAt;
    private UserModel user;
    private BankModel bank;

    @Override
    public String toString() {
        return "TransactionModel{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", bank=" + bank +
                '}';
    }

    public TransactionModel(int id, String label, double amount, String description, Date createdAt, UserModel user, BankModel bank) {
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.user = user;
        this.bank = bank;
    }

    protected TransactionModel(Parcel in) {
        id = in.readInt();
        label = in.readString();
        amount = in.readDouble();
        description = in.readString();
        user = in.readParcelable(UserModel.class.getClassLoader());
        bank = in.readParcelable(BankModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(label);
        dest.writeDouble(amount);
        dest.writeString(description);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(bank, flags);
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UserModel getUser() {
        return user;
    }

    public BankModel getBank() {
        return bank;
    }
}
