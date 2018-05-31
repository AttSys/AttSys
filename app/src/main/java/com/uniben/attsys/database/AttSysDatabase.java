package com.uniben.attsys.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import static com.uniben.attsys.database.AppDatabase.DATABASE_NAME;


public final class AttSysDatabase {

    private static volatile AppDatabase databaseInstance;

    private AttSysDatabase() {
        //no instance
    }

    private static AppDatabase getDatabase(Context context) {
        return Room
                .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        AppDatabase result = databaseInstance;
        if (result == null) {
            synchronized (AttSysDatabase.class) {
                result = databaseInstance;
                if (result == null) {
                    databaseInstance = (result = getDatabase(context));
                }
            }
        }

        return result;
    }
}
