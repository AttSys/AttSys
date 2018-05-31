package com.uniben.attsys.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.Collection;
import java.util.List;



@Dao
public interface BasicDAO<Entity> {
    LiveData<List<Entity>> getAll();

    LiveData<Entity> getItemById(int id);

    /* Inserts */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(final Entity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(final Entity... entities);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(final Collection<Entity> entities);

    /* Deletes */
    @Delete
    int delete(final Entity entity);

    @Delete
    int delete(final Collection<Entity> entities);

    /*@Delete
    int deleteAll();*/

    /* Updates */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(final Entity entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(final Collection<Entity> entities);
}
