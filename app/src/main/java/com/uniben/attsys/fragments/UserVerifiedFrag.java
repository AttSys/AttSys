package com.uniben.attsys.fragments;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.uniben.attsys.AttendanceTakenActivity;
import com.uniben.attsys.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserVerifiedFrag extends Fragment {



    private Button proceedButton;
    private ImageView imageView;

    public UserVerifiedFrag() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.location_verified_layout, container, false);
        imageView = view.findViewById(R.id.animated_check);
        proceedButton = view.findViewById(R.id.proceed_btn);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AttendanceTakenActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        createAnimation();
    }

    private void createAnimation(){

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                imageView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();

    }

}
