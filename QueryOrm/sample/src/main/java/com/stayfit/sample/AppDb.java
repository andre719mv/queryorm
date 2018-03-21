package com.stayfit.sample;

import android.content.Context;

import com.stayfit.queryorm.lib.android.AndroidSQLiteDatabaseHelper;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabase;

public class AppDb extends AndroidSQLiteDatabaseHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DB_RANKMANAGER = "database.db";

    private Context mContext;

    //Required private ctor. Used in DbHelper from orm
    private AppDb() {
        //TODO replace null with some static context. For example: MyApplication.AppContext
        //super(MyApplication.AppContext, DB_RANKMANAGER, DATABASE_VERSION);
        super(null, DB_RANKMANAGER, DATABASE_VERSION);
        mContext = null;
    }

    @Override
    public void onCreate(ISQLiteDatabase db) {
        db.execSQL("some sql");
    }

    @Override
    public void onUpgrade(ISQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("some sql");
        onCreate(db);
    }
}
