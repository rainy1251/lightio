package com.aiyaopai.lightio.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.aiyaopai.lightio.bean.PicBean;

import java.util.List;

@Dao
public interface PicDao {
    @Query("SELECT * FROM picbean")
    List<PicBean> getAll();

    @Query("SELECT * FROM picbean WHERE status Like :status ORDER BY pic_name DESC " )
    List<PicBean> getStatus(int status);

    @Query("SELECT * FROM picbean WHERE pid IN (:picIds)")
    List<PicBean> loadAllByIds(int[] picIds);

    @Query("SELECT * FROM picbean WHERE picId Like :picId")
    PicBean findById(String picId);

    @Query("SELECT * FROM picbean WHERE pic_name Like :name")
    PicBean findByName(String name);

    @Insert
    void insertAll(PicBean... pics);

    @Insert
    void insert(PicBean  pic);

    @Update
    void update(PicBean  pic);

    @Delete
    void delete(PicBean pic);

    @Query("DELETE FROM picbean")
    void delete();
}
    