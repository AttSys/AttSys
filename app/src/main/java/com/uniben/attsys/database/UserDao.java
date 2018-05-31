package com.uniben.attsys.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.uniben.attsys.models.User;

import java.util.List;

/**
 * Created by Cyberman on 5/31/2018.
 */

@Dao
public interface UserDao extends BasicDAO<User>{
    @Override
    @Query("SELECT * FROM User")
    LiveData<List<User>> getAll();

    @Override
    @Query("SELECT * FROM User WHERE id = :id")
    LiveData<User> getItemById(int id);
}
