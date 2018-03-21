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
    private static Class dbHelperClass;
    private static ISQLiteDatabaseHelper instance;

    public static <T extends ISQLiteDatabaseHelper> void setHelperClass(Class<T> helperClass) {
        if(dbHelperClass != null)
            throw new RuntimeException("db helper class already set");

        dbHelperClass = helperClass;
    }

    public static synchronized ISQLiteDatabaseHelper getHelper() {
        if (instance == null) {
            if(dbHelperClass == null)
                throw new RuntimeException("db helper class is not set up");

            try {
                Constructor<?> constructor  = dbHelperClass.getDeclaredConstructors()[0];
                if(Modifier.isPrivate(constructor.getModifiers()))
                    constructor.setAccessible(true);

                instance = (ISQLiteDatabaseHelper)constructor.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException("Bad db helper class present. Instanitiation failed");
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Bad db helper class present. Instanitiation failed");
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException("Bad db helper class present. Instanitiation failed");
            }
        }

        return instance;
    }
}
