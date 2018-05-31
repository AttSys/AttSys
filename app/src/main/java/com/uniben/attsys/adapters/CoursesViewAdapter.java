package com.uniben.attsys.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uniben.attsys.R;
import com.uniben.attsys.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ILENWABOR DAVID on 30/05/2018.
 */

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.CourseViewHolder> {


    private List<Course> courseList;


    public CoursesViewAdapter() {
        courseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.courseTitle.setText(course.getName());
        holder.courseCode.setText(String.valueOf(course.getCode()));
        holder.avatar.setText(extractFirstLetter(course.getName()));
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView avatar, courseTitle, courseCode;
        public CourseViewHolder(View view){
            super(view);
            avatar = view.findViewById(R.id.text_courses_avatar);
            courseCode = view.findViewById(R.id.course_code_list);
            courseTitle = view.findViewById(R.id.course_name);

        }
    }
    public String extractFirstLetter(String string){
        StringBuilder stringBuilder = new StringBuilder(string);
        return stringBuilder.substring(0,1);
    }
}
