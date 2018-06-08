package com.uniben.attsys;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.uniben.attsys.adapters.ViewPagerAdapter;
import com.uniben.attsys.api.AttSysApi;
import com.uniben.attsys.api.ServiceGenerator;
import com.uniben.attsys.dialogs.LoadingDialog;
import com.uniben.attsys.fragments.AttendanceFragment;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.models.Student;
import com.uniben.attsys.models.User;
import com.uniben.attsys.utils.Constants;
import com.uniben.attsys.utils.NotificationUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CoursesActivity extends AppCompatActivity implements AttendanceFragment.OnTakeAttendanceListener{
    private static final String TAG = "CoursesActivity";
    private LoadingDialog loadingDialog;
    ViewPager viewPager;
    TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private User user;
    private boolean doubleBackToExitPressedOnce;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.course_activity_tab_title_attendance));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.course_activity_tab_title_course_list));

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }
                }
        );

        loadingDialog = new LoadingDialog(this, "Getting Data...");
        loadingDialog.show();

        user = getIntent().getParcelableExtra(Constants.USER_KEY);

        getData();
    }

    private void getData() {
        AttSysApi attSysApi = ServiceGenerator.createService(AttSysApi.class);
        attSysApi.getStudent("Token " + user.getToken().getToken()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubsciber());
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    private void navigateBack() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }
        this.doubleBackToExitPressedOnce = true;
        NotificationUtils.notifyUser(viewPager, getString(R.string.exit_app_message));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, Constants.EXIT_APP_DURATION);
    }

    private Observer<Student> getSubsciber() {
        return new DisposableObserver<Student>() {
            @Override
            public void onNext(Student studentUser) {
                loadingDialog.dismissWithAnimation();
                student = studentUser;
                if (student != null) {
                    Log.i(TAG, "onNext: " + student.getAttendanceList().size());
                    adapter.setAttendaceList(student.getAttendanceList());
                    adapter.setCourseList(student.getCourseList());
                }else{
                    loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    loadingDialog.setContentText("An error occurred");
                    loadingDialog.setConfirmText("Retry");
                    loadingDialog.setConfirmClickListener(sweetAlertDialog -> {
                        getData();
                    });
                    NotificationUtils.notifyUser(viewPager, "An error occurred!");
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                loadingDialog.setContentText("An error occurred");
                loadingDialog.setConfirmText("Retry");
                loadingDialog.setConfirmClickListener(sweetAlertDialog -> {
                    getData();
                });
                e.printStackTrace();
                NotificationUtils.notifyUser(viewPager, "An error occurred!");
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onTakeAttendace(Attendance attendance) {
       // NotificationUtils.notifyUser(viewPager, "Attendance section is in development");

        Intent intent = new Intent(this, AttendanceActivity.class);
        intent.putExtra(Constants.ATTENDANCE_KEY, attendance);
        startActivity(intent);
    }
}
