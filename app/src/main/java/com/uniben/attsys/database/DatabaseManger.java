package com.uniben.attsys.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;


import com.uniben.attsys.models.User;

import java.util.List;


import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Cyberman on 4/4/2018.
 */

public class DatabaseManger {

    private UserDao userDao;


    public DatabaseManger(Context context) {
        AppDatabase appDatabase = AttSysDatabase.getInstance(context);
        userDao = appDatabase.userDao();
    }


    /**
     * Get the user data
     * @return the user data wrapped in a live data object
     * */
    public LiveData<List<User>> getUserData(){
        return userDao.getAll();
    }

    public Single<Long> insertUserData(User user){
        return Single.fromCallable(() -> userDao.insert(user))
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> updateUserData(User user){
        return Single.fromCallable(() -> userDao.update(user))
                .subscribeOn(Schedulers.io());
    }



}
