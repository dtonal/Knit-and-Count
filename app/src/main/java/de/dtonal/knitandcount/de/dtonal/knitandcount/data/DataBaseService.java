package de.dtonal.knitandcount.de.dtonal.knitandcount.data;

import android.content.Context;

import androidx.room.Room;

import static de.dtonal.knitandcount.de.dtonal.knitandcount.data.AppDatabase.MIGRATION_1_2;
import static de.dtonal.knitandcount.de.dtonal.knitandcount.data.AppDatabase.MIGRATION_2_3;

public class DataBaseService {
    private static AppDatabase db;

    public static AppDatabase getOrInitAppDataBase(Context applicationContext){
        if(db == null){
            db = Room.databaseBuilder(applicationContext,
                    AppDatabase.class, "database-main")
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return db;
    }
}
