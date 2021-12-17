package com.aiyaopai.lightio.view;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.db.PicDao;

@Database(entities = {PicBean.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
        public abstract PicDao picDao();
    }