package com.uniben.attsys.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class Attendance implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("course")
    private Course course;

    @SerializedName("venue")
    private Venue venue;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.course, flags);
        dest.writeParcelable(this.venue, flags);
    }

    public Attendance() {
    }

    protected Attendance(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.course = in.readParcelable(Course.class.getClassLoader());
        this.venue = in.readParcelable(Venue.class.getClassLoader());
    }

    public static final Parcelable.Creator<Attendance> CREATOR = new Parcelable.Creator<Attendance>() {
        @Override
        public Attendance createFromParcel(Parcel source) {
            return new Attendance(source);
        }

        @Override
        public Attendance[] newArray(int size) {
            return new Attendance[size];
        }
    };
}
