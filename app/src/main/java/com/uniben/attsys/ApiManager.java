package com.uniben.attsys;

import com.uniben.attsys.api.AttSysApi;
import com.uniben.attsys.api.ServiceGenerator;
import com.uniben.attsys.models.Token;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class ApiManager {
    public static void validateUser(String username, String password){
        AttSysApi attSysApi = ServiceGenerator.createService(AttSysApi.class);
        attSysApi.authUser(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getUserValidationSubscriber());
    }

    private static Observer<Token> getUserValidationSubscriber() {
        return new Observer<Token>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Token token) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
