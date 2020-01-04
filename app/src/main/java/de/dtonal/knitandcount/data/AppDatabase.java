package de.dtonal.knitandcount.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.dtonal.knitandcount.data.converter.Converters;
import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.dao.PdfStateDao;
import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.data.model.PdfState;
import de.dtonal.knitandcount.data.model.Project;

@Database(entities = {Project.class, Counter.class, PdfState.class}, version = 7)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProjectDao projectDao();
    public abstract CounterDao counterDao();

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `pdfstate` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `zoom` REAL NOT NULL, `offsetX` REAL NOT NULL, `offsetY` REAL NOT NULL, `project_id` INTEGER NOT NULL, FOREIGN KEY(`project_id`) REFERENCES `Project`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_pdfstate_project_id` ON pdfstate (`project_id`)");
        }
    };

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

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE project add column notes TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE project add column gauge_dry TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE project add column gauge_wet TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE project add column size TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE project add column yardage TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE project add column needleSize TEXT DEFAULT ''");
        }
    };

    public abstract PdfStateDao pdfStateDao();
}
