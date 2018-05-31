package com.uniben.attsys.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.uniben.attsys.database.DatabaseManger;
import com.uniben.attsys.models.User;

import java.util.List;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class UserViewModel extends AndroidViewModel {
    private LiveData<List<User>> userListLiveData;
    public UserViewModel(@NonNull Application application) {
        super(application);
        DatabaseManger databaseManger = new DatabaseManger(application);
        userListLiveData = databaseManger.getUserData();
    }

    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }
}
