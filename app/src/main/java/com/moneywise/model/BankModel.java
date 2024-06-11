package com.moneywise.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BankModel implements Parcelable, Serializable {

    private final int id;
    private String name;

    public BankModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected BankModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public static final Creator<BankModel> CREATOR = new Creator<BankModel>() {
        @Override
        public BankModel createFromParcel(Parcel in) {
            return new BankModel(in);
        }

        @Override
        public BankModel[] newArray(int size) {
            return new BankModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
