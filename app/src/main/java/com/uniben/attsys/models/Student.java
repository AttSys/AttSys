package com.uniben.attsys.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class Student implements Parcelable {
    @SerializedName("user")
    private int id;

    @SerializedName("picture")
    private String picture;

    @SerializedName("courses")
    private List<Course> courseList;

    @SerializedName("ongoing_attendance")
    private List<Attendance> attendanceList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.picture);
        dest.writeList(this.courseList);
        dest.writeList(this.attendanceList);
    }

    public Student() {
    }

    protected Student(Parcel in) {
        this.id = in.readInt();
        this.picture = in.readString();
        this.courseList = new ArrayList<Course>();
        in.readList(this.courseList, Course.class.getClassLoader());
        this.attendanceList = new ArrayList<Attendance>();
        in.readList(this.attendanceList, Attendance.class.getClassLoader());
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
