package de.dtonal.knitandcount.data;

import android.content.Context;

import androidx.room.Room;

import static de.dtonal.knitandcount.data.AppDatabase.MIGRATION_1_2;
import static de.dtonal.knitandcount.data.AppDatabase.MIGRATION_2_3;
import static de.dtonal.knitandcount.data.AppDatabase.MIGRATION_3_4;
import static de.dtonal.knitandcount.data.AppDatabase.MIGRATION_4_5;
import static de.dtonal.knitandcount.data.AppDatabase.MIGRATION_5_6;
import static de.dtonal.knitandcount.data.AppDatabase.MIGRATION_6_7;

public class DataBaseService {
    private static AppDatabase db;

    public static AppDatabase getOrInitAppDataBase(Context applicationContext){
        if(db == null){
            db = Room.databaseBuilder(applicationContext,
                    AppDatabase.class, "database-main")
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .addMigrations(MIGRATION_3_4)
                    .addMigrations(MIGRATION_4_5)
                    .addMigrations(MIGRATION_5_6)
                    .addMigrations(MIGRATION_6_7)
                    .build();
        }
        return db;
    }
}
