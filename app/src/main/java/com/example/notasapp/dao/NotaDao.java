package com.example.notasapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notasapp.entidades.Nota;

import java.util.List;

@Dao
public interface NotaDao {

    @Query("SELECT * FROM notas ORDER BY id DESC")
    List<Nota> getAllNotas();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNota(Nota nota);

    @Delete
    void deleteNota(Nota nota);
}
