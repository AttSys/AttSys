package com.uniben.attsys.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.uniben.attsys.models.User;


/**
 * Created by Cyberman on 4/2/2018.
 */
@TypeConverters({Converters.class})
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    static final String DATABASE_NAME = "att_sys.db";



    public abstract UserDao userDao();



}
