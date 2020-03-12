package com.example.univerlabs_6;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class RemainderDAO {
    @Query("SELECT * FROM (SELECT * from Remainder ORDER BY id DESC LIMIT 10) ORDER BY id ASC")
    abstract List<Remainder> getPage();

    @Delete
    abstract void delete(Remainder remainder);

    @Insert
    abstract void insert(Remainder remainder);
}
