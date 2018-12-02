package main.mobieleappsapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DatabaseAccess {

    @Insert
    void insert(DatabaseMessage dm);
    @Query("SELECT * FROM DatabaseMessage ORDER BY id ASC")
    List<DatabaseMessage> getAll ();
    @Query("DELETE FROM DatabaseMessage")
    void deleteAll ();
}
