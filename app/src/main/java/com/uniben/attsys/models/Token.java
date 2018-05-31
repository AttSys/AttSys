package com.uniben.attsys.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class Token implements Parcelable {
    @SerializedName("token")
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
    }

    protected Token(Parcel in) {
        this.token = in.readString();
    }

    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}
