package com.moneywise.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements Parcelable, Serializable {
    private final int id;
    private final String email;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public UserModel(int id, String email) {
        this.id = id;
        this.email = email;
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        email = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(email);
    }
}
