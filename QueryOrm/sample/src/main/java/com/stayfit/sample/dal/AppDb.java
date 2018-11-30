package com.stayfit.sample.dal;
import android.content.Context;

import com.stayfit.queryorm.lib.android.AndroidSQLiteDatabaseHelper;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabase;

public class AppDb extends AndroidSQLiteDatabaseHelper {
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "database.db";

    public AppDb(Context context) {
        super(context, DB_NAME, DB_VERSION);
    }

    @Override
    public void onCreate(ISQLiteDatabase db) {
        db.execSQL(CreateTableScripts.Person);
        db.execSQL(CreateTableScripts.Exercise);
    }

    @Override
    public void onUpgrade(ISQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(CreateTableScriptsOld.Person1);
            db.execSQL(String.format("ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0", Tables.Person, DbCoumns.Person.Age));
        }

        if (oldVersion < 3) {
            db.execSQL(String.format("ALTER TABLE %s ADD COLUMN %s TEXT", Tables.Person, DbCoumns.Person.LastName));
        }

        onCreate(db);
    }
}
