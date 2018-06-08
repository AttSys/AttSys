package com.uniben.attsys;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.uniben.attsys.api.AttSysApi;
import com.uniben.attsys.api.ServiceGenerator;
import com.uniben.attsys.database.DatabaseManger;
import com.uniben.attsys.dialogs.LoadingDialog;
import com.uniben.attsys.models.Token;
import com.uniben.attsys.models.User;
import com.uniben.attsys.utils.Constants;
import com.uniben.attsys.utils.validations.Validations;
import com.uniben.attsys.viewmodels.UserViewModel;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    @BindViews({R.id.edit_username, R.id.edit_password})
    List<TextInputEditText> inputEditTexts;
    private LoadingDialog loadingDialog;
    private Disposable disposable;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activtiy);
        ButterKnife.bind(this);

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUserListLiveData().observe(this, users -> {

            if (users != null) {
                if(users.size() >0){
                    user = users.get(0);
                    startNewActivity(user);
                }else{
                    user = new User();
                }
            }
        });



    }

    private void startNewActivity(User user) {
        Intent intent = new Intent(LoginActivity.this, CoursesActivity.class);
        intent.putExtra(Constants.USER_KEY, user);
        startActivity(intent);
        finish();
    }


    public void dummyLogin(View view) {
        if (Validations.validateUserData(inputEditTexts)) {
            loadingDialog = new LoadingDialog(this, "Validating");
            loadingDialog.show();

            String username = inputEditTexts.get(0).getText().toString();
            String password = inputEditTexts.get(1).getText().toString();
            user.setPassword(password);
            user.setUsername(username);
            //send to api
            AttSysApi attSysApi = ServiceGenerator.createService(AttSysApi.class);
            attSysApi.authUser(username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserValidationSubscriber());
        }

    }


    private Observer<Token> getUserValidationSubscriber() {
        return new Observer<Token>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;

            }

            @Override
            public void onNext(Token token) {
                loadingDialog.dismissWithAnimation();
                if (token != null) {
                    user.setToken(token);
                    new DatabaseManger(LoginActivity.this)
                            .insertUserData(user)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                    startNewActivity(user);
                }else{
                    loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    loadingDialog.setContentText("Invalid credentials");
                    loadingDialog.setConfirmText("Cancel");
                    loadingDialog.setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                loadingDialog.setContentText("An error occurred");
                loadingDialog.setConfirmText("Cancel");
                loadingDialog.setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);
            }

            @Override
            public void onComplete() {

            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
