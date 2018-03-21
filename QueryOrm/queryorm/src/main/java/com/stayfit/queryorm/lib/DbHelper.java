package com.stayfit.queryorm.lib;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabaseHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Created by Администратор on 5/25/2017.
 */

public class DbHelper {
    private static ISQLiteDatabaseHelper instance;

    public static <T extends ISQLiteDatabaseHelper> void setHelper(ISQLiteDatabaseHelper helper) {
        instance = helper;
    }

    public static synchronized ISQLiteDatabaseHelper getHelper() {
        if(instance == null)
            throw new RuntimeException("db helper class is not set up");

        return instance;
    }
}
