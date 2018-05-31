package com.uniben.attsys.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniben.attsys.R;
import com.uniben.attsys.adapters.AttendenceListAdapter;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.views.EmptyRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.internal.observers.EmptyCompletableObserver;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment implements AttendenceListAdapter.OnTakeAttendanceListener {

    @BindView(R.id.rv_attedance)
    EmptyRecyclerView emptyRecyclerView;

    @BindView(R.id.empty_root)
    View noDataView;

    private AttendenceListAdapter adapter;

    private OnTakeAttendanceListener onTakeAttendanceListener;

    public void setOnTakeAttendanceListener(OnTakeAttendanceListener onTakeAttendanceListener) {
        this.onTakeAttendanceListener = onTakeAttendanceListener;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onTakeAttendanceListener = (OnTakeAttendanceListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onTakeAttendanceListener = null;
    }

    public AttendanceFragment() {
        // Required empty public constructor

    }

    public void setAttendanceList(List<Attendance> attendenceList){
        adapter.setAttendanceList(attendenceList);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);;
        ButterKnife.bind(this, view);

        adapter = new AttendenceListAdapter();
        adapter.setOnTakeAttendanceListener(this);
        emptyRecyclerView.setAdapter(adapter);
        emptyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyRecyclerView.setEmptyView(noDataView);
        return view;
    }

    @Override
    public void onTakeAttendace(Attendance attendance) {
        onTakeAttendanceListener.onTakeAttendace(attendance);
    }

    public interface  OnTakeAttendanceListener{
        void onTakeAttendace(Attendance attendance);
    }
}
