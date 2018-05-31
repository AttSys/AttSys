package com.uniben.attsys.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniben.attsys.R;
import com.uniben.attsys.adapters.CoursesViewAdapter;
import com.uniben.attsys.models.Course;
import com.uniben.attsys.views.ItemDivider;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CoursesFragment extends Fragment {


    private RecyclerView recyclerView;
    private CoursesViewAdapter coursesViewAdapter;



    public CoursesFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setCourseList(List<Course> courseList) {
        coursesViewAdapter.setCourseList(courseList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        recyclerView = view.findViewById(R.id.courses_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        coursesViewAdapter = new CoursesViewAdapter();
        recyclerView.setAdapter(coursesViewAdapter);
        return view;
    }

}
