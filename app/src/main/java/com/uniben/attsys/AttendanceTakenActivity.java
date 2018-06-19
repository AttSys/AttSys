package com.uniben.attsys;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.uniben.attsys.models.User;
import com.uniben.attsys.utils.Constants;
import com.uniben.attsys.viewmodels.UserViewModel;

public class AttendanceTakenActivity extends AppCompatActivity {

    private Button homeButton;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_taken_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        homeButton = findViewById(R.id.home_btn);
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUserListLiveData().observe(this, users -> {
            if (users != null) {
                if(users.size() >0){
                    user = users.get(0);
                }
            }
                    });

            homeButton.setOnClickListener(v -> startNewActivity(user));
    }


    private void startNewActivity(User user) {
        Intent intent = new Intent(AttendanceTakenActivity.this, CoursesActivity.class);
        intent.putExtra(Constants.USER_KEY, user);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        startNewActivity(user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            startNewActivity(user);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

