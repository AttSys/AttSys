package com.uniben.attsys.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.uniben.attsys.fragments.AttendanceFragment;
import com.uniben.attsys.fragments.CoursesFragment;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.models.Course;

import java.util.List;

/**
 * Created by ILENWABOR DAVID on 30/05/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;
    private AttendanceFragment attendanceFragment;
    private CoursesFragment coursesFragment;

    public ViewPagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;


    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                if (attendanceFragment != null) {
                    return attendanceFragment;
                }else{
                    attendanceFragment = new AttendanceFragment();
                    return attendanceFragment;

                }
            case 1:  if (coursesFragment != null) {
                return coursesFragment;
            }else{
                coursesFragment = new CoursesFragment();
                return coursesFragment;

            }
        }
        return null;
    }

    public void setCourseList(List<Course> courseList){
        coursesFragment.setCourseList(courseList);
    }

    public void setAttendaceList(List<Attendance> attendaceList){
        attendanceFragment.setAttendanceList(attendaceList);
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
