package com.example.secondchance.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.secondchance.MyApplicaion;

@Database(entities = {User.class,Post.class}, version = 4)
    abstract class AppLocalDbRepository extends RoomDatabase {
        public abstract UserDao userDao();
        public abstract PostDao postDao();
    }
    public class AppLocalDb{
        static public AppLocalDbRepository db =
                Room.databaseBuilder(MyApplicaion.context,
                        AppLocalDbRepository.class,
                        "ContextDb13.db")
                        .fallbackToDestructiveMigration()
                        .build();
    };

