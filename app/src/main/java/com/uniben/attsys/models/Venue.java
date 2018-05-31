package com.uniben.attsys.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class Venue implements Parcelable {
    @SerializedName("name")
    private String name;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("radius")
    private int radius;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.radius);
    }

    public Venue() {
    }

    protected Venue(Parcel in) {
        this.name = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.radius = in.readInt();
    }

    public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel source) {
            return new Venue(source);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };
}
