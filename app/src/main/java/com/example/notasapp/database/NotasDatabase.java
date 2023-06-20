package com.example.notasapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notasapp.dao.NotaDao;
import com.example.notasapp.entidades.Nota;

@Database(entities = Nota.class, version = 1, exportSchema = false)
public abstract class NotasDatabase extends RoomDatabase {

    private static NotasDatabase notasDatabase;

    public static synchronized NotasDatabase getDatabase(Context context) {
        if (notasDatabase == null) {
            notasDatabase = Room.databaseBuilder(
                    context,
                    NotasDatabase.class,
                    "notas_db"
            ).build();
        }
        return notasDatabase;
    }

    public abstract NotaDao notaDao();
}
