package com.stayfit.sample.dal;
import android.content.Context;

import com.stayfit.queryorm.lib.android.AndroidSQLiteDatabaseHelper;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabase;

public class AppDb extends AndroidSQLiteDatabaseHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "database.db";

    public AppDb(Context context) {
        super(context, DB_NAME, DB_VERSION);
    }

    @Override
    public void onCreate(ISQLiteDatabase db) {
        db.execSQL(CreateTableScripts.Person);
    }

    @Override
    public void onUpgrade(ISQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(CreateTableScriptsOld.Person1);
            db.execSQL(String.format("ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0", Tables.Person, DbCoumns.Person.Age));
        }

        onCreate(db);
    }
}
