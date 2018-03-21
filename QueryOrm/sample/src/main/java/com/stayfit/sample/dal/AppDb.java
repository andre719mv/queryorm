package com.stayfit.sample.dal;

import android.content.Context;

import com.stayfit.queryorm.lib.android.AndroidSQLiteDatabaseHelper;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabase;
import com.stayfit.sample.MyApplication;

public class AppDb extends AndroidSQLiteDatabaseHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "database.db";

    private Context mContext;

    //Required private ctor. Used in DbHelper from orm
    private AppDb() {
        //Some static context. For example: MyApplication.AppContext
        super(MyApplication.AppContext, DB_NAME, DB_VERSION);
        mContext = MyApplication.AppContext;
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
