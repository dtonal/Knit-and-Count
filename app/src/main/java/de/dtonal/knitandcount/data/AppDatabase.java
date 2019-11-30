package de.dtonal.knitandcount.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.dtonal.knitandcount.data.converter.Converters;
import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.data.model.Project;

@Database(entities = {Project.class, Counter.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProjectDao projectDao();
    public abstract CounterDao counterDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE project add column creation_date INTEGER");
        }
    };


    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `COUNTER` (`counter_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `creation_date` INTEGER, `value` INTEGER NOT NULL, `reset_value` INTEGER NOT NULL, `project_id` INTEGER NOT NULL, FOREIGN KEY(`project_id`) REFERENCES `Project`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_Project_id` ON Project (`id`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_Counter_project_id` ON Counter (`project_id`)");
        }
    };


    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DELETE FROM counter");
        }
    };
}
