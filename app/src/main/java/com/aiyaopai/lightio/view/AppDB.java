package com.aiyaopai.lightio.view;

import androidx.room.Room;

import com.aiyaopai.lightio.util.UiUtils;

public class AppDB {

    private volatile static AppDatabase db;

    private AppDB() {
    }

    public static AppDatabase getInstance() {

        if (db == null) {

            synchronized ( AppDB.class) {

                if (db == null) {

                    db = Room.databaseBuilder(UiUtils.getContext(),
                            AppDatabase.class, "database-name")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();

                }

            }

        }

        return db;

    }
}