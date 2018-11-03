package com.stayfit.queryorm.lib.android;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteContentValues;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteCursor;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabase;

/**
 * Created by Администратор on 3/21/2018.
 */

public class AndroidSQLiteDatabase extends ISQLiteDatabase {
    private SQLiteDatabase db;

    public AndroidSQLiteDatabase(SQLiteDatabase db) {
        if (db == null) {
            throw new IllegalArgumentException("dbHandle can't be null");
        }
        this.db = db;
    }

    @Override
    public ISQLiteContentValues newContentValues() {
        return new AndroidContentValues();
    }

    @Override
    public ISQLiteCursor query(String table, String[] columns,
                               String selection, String[] selectionArgs, String groupBy,
                               String having, String orderBy) {
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        if (cursor == null) {
            return null;
        }
        return new AndroidCursor(cursor);
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        return db.delete(table, whereClause, whereArgs);
    }

    @Override
    public long insert(String table, String nullColumnHack,
                       ISQLiteContentValues initialValues) {
        ContentValues values = convertFromValues(initialValues);
        return db.insert(table, nullColumnHack, values);
    }

    private ContentValues convertFromValues(ISQLiteContentValues initialValues) {
        return ((AndroidContentValues)initialValues).getContentValues();
    }

    @Override
    public void update(String tableName, ISQLiteContentValues values,
                       String whereClause, String[] whereArgs) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }

        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");

        // move all bind args to one array
        int setValuesSize = values.size();
        int bindArgsSize = (whereArgs == null) ? setValuesSize : (setValuesSize + whereArgs.length);
        Object[] bindArgs = new Object[bindArgsSize];
        int i = 0;
        for (String colName : values.keySet()) {
            sql.append((i > 0) ? "," : "");
            sql.append(colName);
            bindArgs[i++] = values.get(colName);
            sql.append("=?");
        }
        if (whereArgs != null) {
            for (i = setValuesSize; i < bindArgsSize; i++) {
                bindArgs[i] = whereArgs[i - setValuesSize];
            }
        }
        if (!TextUtils.isEmpty(whereClause)) {
            sql.append(" WHERE ");
            sql.append(whereClause);
        }

        SQLiteStatement stmt = db.compileStatement(sql.toString());
        int idx = 0;
        for (Object bind : bindArgs) {
            idx ++;
            if (bind instanceof String) {
                stmt.bindString(idx, (String) bind);
            } else if (bind instanceof Integer) {
                stmt.bindLong(idx, (Integer) bind);
            } else if (bind instanceof Long) {
                stmt.bindLong(idx, (Long) bind);
            }
        }
        stmt.executeUpdateDelete();
    }

    @Override
    public void execSQL(String statement) {
        db.execSQL(statement);
    }

    @Override
    public ISQLiteCursor rawQuery(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor == null ? null : new AndroidCursor(cursor);
    }
}
