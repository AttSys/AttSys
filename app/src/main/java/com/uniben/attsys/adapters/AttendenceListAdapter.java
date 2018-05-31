package com.uniben.attsys.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uniben.attsys.R;
import com.uniben.attsys.models.Attendance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class AttendenceListAdapter extends RecyclerView.Adapter<AttendenceListAdapter.AttendanceListViewHolder> {

    private List<Attendance> attendanceList;
    private OnTakeAttendanceListener onTakeAttendanceListener;

    public AttendenceListAdapter() {
        attendanceList = new ArrayList<>();
    }

    public void setOnTakeAttendanceListener(OnTakeAttendanceListener onTakeAttendanceListener) {
        this.onTakeAttendanceListener = onTakeAttendanceListener;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendanceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendanceListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_ongoing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.textViews.get(0).setText(attendance.getCourse().getName());
        holder.textViews.get(1).setText(attendance.getCourse().getCode());
        holder.textViews.get(2).setText(String.valueOf(attendance.getCourse().getCredits()));
        holder.textViews.get(4).setText(String.valueOf(attendance.getCourse().getDescription()));
        holder.takeAttendanceButton.setTag(attendance);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    class AttendanceListViewHolder extends RecyclerView.ViewHolder{
        @BindViews({ R.id.course_title, R.id.course_code, R.id.course_credit, R.id.week_number, R.id.course_description})
        List<TextView> textViews;

        @BindView(R.id.take_attendance_btn)
        Button takeAttendanceButton;

        public AttendanceListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.take_attendance_btn)
        public void takeAttendence(View view){
            if (onTakeAttendanceListener != null) {
                onTakeAttendanceListener.onTakeAttendace((Attendance) view.getTag());
            }
        }
    }


    public interface  OnTakeAttendanceListener{
        void onTakeAttendace(Attendance attendance);
    }
}

