package com.example.secondchance.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from User")
    List<User> getAllUsers();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... students);
    @Delete
    void delete(User student);
}
