package com.stayfit.queryorm.lib;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqlExecutor {
	private SQLiteDatabase db;

	public SqlExecutor(SQLiteDatabase db){
		this.db = db;
	}
	public SqlExecutor(){
		db = DbHelper.getHelper().getWritableDatabase();
	}
	public QueryResult executeSelect(QueryParms queryParms){
		SmartSqlQuery query = QueryBuilder.buildSql(queryParms);
		Cursor cursor = db.rawQuery(query.getSql(), query.getArgs());
		return new QueryResult(cursor,queryParms.getEntityType());
	}

	public <T> QueryResult executeSelect(Class<T> cl, String sql){
		Cursor cursor = db.rawQuery(sql, null);
		return new QueryResult(cursor, cl);
	}
	
	public Cursor rawQuery(String sql){
		return db.rawQuery(sql, null);
	}

	public int delete(QueryParms queryParms){
		SmartSqlQuery query = QueryBuilder.builDeleteSql(queryParms);
		Cursor cursor = db.rawQuery(query.getSql(), query.getArgs());
		cursor.moveToFirst();
		int rowsAffected = 0;
		if(cursor.getCount()> 0)
			rowsAffected = cursor.getInt(0);
		cursor.close();
		return rowsAffected;
	}

	public void insert(String table, String nullColumnHack, ContentValues values) {
		db.insert(table, nullColumnHack, values);
	}
}
