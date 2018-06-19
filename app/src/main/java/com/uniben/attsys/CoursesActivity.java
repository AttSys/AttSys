package com.uniben.attsys;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.uniben.attsys.adapters.ViewPagerAdapter;
import com.uniben.attsys.api.AttSysApi;
import com.uniben.attsys.api.ServiceGenerator;
import com.uniben.attsys.database.DatabaseManger;
import com.uniben.attsys.dialogs.LoadingDialog;
import com.uniben.attsys.fragments.AttendanceFragment;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.models.Student;
import com.uniben.attsys.models.User;
import com.uniben.attsys.utils.Constants;
import com.uniben.attsys.utils.NotificationUtils;
import com.uniben.attsys.views.FlipPageViewTransformer;

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
        viewPager.setPageTransformer(false, new FlipPageViewTransformer());

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



        user = getIntent().getParcelableExtra(Constants.USER_KEY);

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login_activtiy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            displayConfirmDialog();
        }else if(item.getItemId() == R.id.action_refresh){
            getData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayConfirmDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.log_out_title_text));
        sweetAlertDialog.setContentText(getString(R.string.log_out_msg_text));
        sweetAlertDialog.setConfirmText(getString(android.R.string.ok));
        sweetAlertDialog.setCancelText(getString(android.R.string.cancel));
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            logUserOut();
            sweetAlertDialog1.dismissWithAnimation();
        });
        sweetAlertDialog.setCancelClickListener(SweetAlertDialog::dismissWithAnimation);
        sweetAlertDialog.show();
    }

    private void logUserOut() {
        DatabaseManger databaseManger = new DatabaseManger(this);
        databaseManger.deleteUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void getData() {
        loadingDialog = new LoadingDialog(this, "Getting Data...");
        loadingDialog.show();
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
                    NotificationUtils.notifyUser(viewPager, "An error occurred null student!");
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                loadingDialog.setContentText("An error occurred");
                loadingDialog.setConfirmText("Retry");
                loadingDialog.setConfirmClickListener(sweetAlertDialog -> {
                    getData();
                    sweetAlertDialog.dismiss();
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
