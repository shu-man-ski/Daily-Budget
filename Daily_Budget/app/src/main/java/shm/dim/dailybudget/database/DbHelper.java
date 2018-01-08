package shm.dim.dailybudget.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BudgetDb.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table if not exists Category ( "
                + "NAME text primary key);");

        database.execSQL("create table if not exists Costs ( "
                + "ID_COSTS integer primary key autoincrement, "
                + "COST text not null, "
                + "DATE text not null, "
                + "CATEGORY text not null, "
                + "constraint FK_CATEGORY foreign key(CATEGORY) references Category(NAME) on delete cascade on update cascade);");

        database.execSQL("create index if not exists idx_сosts "
                + "on Costs(CATEGORY);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table Category;");
        database.execSQL("DROP TABLE Costs;");
        onCreate(database);
    }
}