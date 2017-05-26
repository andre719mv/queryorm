package com.stayfit.queryorm.lib;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.annimon.stream.Stream;
import com.google.firebase.crash.FirebaseCrash;

public class DOBase {
	public long _id = -1;

	protected DOBase() {
	}

	protected DOBase(Cursor cursor) {
		Map<String, String> mapppedColumns = new HashMap<>();
		Class c = this.getClass();
		for(Field f : c.getFields()){
			if(f.isAnnotationPresent(MapColumn.class))
				mapppedColumns.put(f.getAnnotation(MapColumn.class).value(), f.getName());
		}


		String[] columns = cursor.getColumnNames();
		String tableName = getTableName();
		for (String column : columns) {
			try {
				String fieldName;
				if(mapppedColumns.containsKey(column))
					fieldName = mapppedColumns.get(column);
				else
					fieldName = column.replace("_" + tableName, "");

				Field f = c.getField(fieldName);
				String fieldType = f.getType().getName();

				if (fieldType.equals("java.lang.String")) {
					if(!cursor.isNull(cursor.getColumnIndex(column)))
						f.set(this, cursor.getString(cursor.getColumnIndex(column)));
				} else if (fieldType.equals("int")) {
					f.setInt(this, cursor.getInt(cursor.getColumnIndex(column)));
				} else if (fieldType.equals("boolean")) {
					f.setBoolean(this,	cursor.getInt(cursor.getColumnIndex(column)) == 1);
				} else if (fieldType.equals("long")) {
					f.setLong(this,	cursor.getLong(cursor.getColumnIndex(column)));
				} else if (fieldType.equals("java.util.Date")) {
					f.set(this,	new TypeConverter().readDate(cursor.getString(cursor.getColumnIndex(column))));
				}else if (fieldType.equals("double")) {
					f.set(this,	cursor.getDouble(cursor.getColumnIndex(column)));
				}else if (fieldType.equals("java.lang.Double")) {
					if(!cursor.isNull(cursor.getColumnIndex(column)))
						f.set(this,	cursor.getDouble(cursor.getColumnIndex(column)));
				}
			} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
				FirebaseCrash.report(e);
			}
		}
	}

	//public static <T> T SelectById(T t, Long id) {
	//	return SelectById(t.getClass());
	//}
	
	public static <T> T selectById(Class<T> cl, Long id) {
		SqlExecutor executor = new SqlExecutor();

		QueryParms parms = new QueryParms(cl)
				.addCriteria("_id", id.toString())
				.withDeleted();
		QueryResult result = executor.executeSelect(parms);
		T obj = result.selectSingle(true);
		return obj;
	}

	public static <T> T selectByColumnVal(Class<T> cl, String columnName, Object value) {
		SqlExecutor executor = new SqlExecutor();

		QueryParms parms = new QueryParms(cl).addCriteria(columnName, value.toString());
		QueryResult result = executor.executeSelect(parms);
		T obj = result.selectSingle(true);
		return obj;
	}

    public static <T> List<T> selectAllByColumnVal(Class<T> cl, String columnName, Object value) {
        SqlExecutor executor = new SqlExecutor();

        QueryParms parms = new QueryParms(cl).addCriteria(columnName, value.toString());
        QueryResult result = executor.executeSelect(parms);
        List<T> obj = result.selectAll(true);
        return obj;
    }

	public static <T> T selectSingle(Class<T> cl, QueryParms parms) {
		SqlExecutor executor = new SqlExecutor();

		QueryResult result = executor.executeSelect(parms);
		T obj = result.selectSingle(true);
		return obj;
	}
	
	public static <T> List<T> selectAll(Class<T> cl, QueryParms parms) {
		SqlExecutor executor = new SqlExecutor();

		QueryResult result = executor.executeSelect(parms);
		List<T> obj = result.selectAll(true);
		return obj;
	}

	public static <T> List<T> selectAll(Class<T> cl, String sql) {
		SqlExecutor executor = new SqlExecutor();

		QueryResult result = executor.executeSelect(cl, sql);
		List<T> obj = result.selectAll(true);
		return obj;
	}

	public static String getTableName(Class cl) {
		if(cl.isAnnotationPresent(MapTable.class))
			return ((MapTable)cl.getAnnotation(MapTable.class)).value();
		else
			return cl.getSimpleName().toLowerCase();
	}

	private String getTableName() {
		return getTableName(this.getClass());
	}

	public Long save() { return save(DbHelper.getHelper().getWritableDatabase(), true); }

	public Long save(SQLiteDatabase db, boolean closeConnection) {
		Class c = this.getClass();
		String tableName = getTableName();

		ContentValues newValues = new ContentValues();

		try {
			for (Field f : c.getFields()) {
				//ignore voilotaile $change  and id
				if (!f.getName().equals(CommonFields.Id)
						&& !f.getName().startsWith("$")
						&& !f.getName().toLowerCase().startsWith("serialversionuid")
						&& !f.isAnnotationPresent(ColumnDeleted.class))
				{
					String valueToSet = "";
					if (f.getType().getName().equals("boolean"))
						valueToSet = Boolean.valueOf(f.get(this).toString())? "1":"0";
					else if(f.getType().getName().equals("java.util.Date"))
                        valueToSet = new TypeConverter().writeDateTime((Date)f.get(this));
                    else
						valueToSet = f.get(this) == null ? null: f.get(this).toString();

					String columnName;
					if(f.isAnnotationPresent(MapColumn.class))
						columnName = f.getAnnotation(MapColumn.class).value();
					else
						columnName = f.getName().toLowerCase() + "_" + tableName;

					newValues.put(columnName, valueToSet);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			FirebaseCrash.report(e);
		}

		if (_id >= 0) {
			newValues.put(CommonFields.Id, _id);
			String[] whereArgs = new String[]{String.valueOf(_id)};
			db.update(tableName, newValues, CommonFields.Id + " = ?", whereArgs);
		} else {
			_id = db.insert(tableName, null, newValues);
		}
		//if(closeConnection)
		//	db.close();
		return _id;
	}
	
	public void delete() {
		if (_id < 0)
			return;

		try {
			Class c = this.getClass();
			Field isDeletedField = Stream.of(c.getFields())
					.filter(x-> x.isAnnotationPresent(MapColumn.class)
							&& x.getAnnotation(MapColumn.class).value().equals(CommonFields.IsDeleted)
					)
					.findSingle()
					.orElse(null);

			if(isDeletedField != null) {
				isDeletedField.setBoolean(this, true);
				save();
				return;
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			FirebaseCrash.report(e);
		}

		deleteForever();
	}

	public void deleteForever() {
		if (_id < 0)
			return;

		SQLiteDatabase db = DbHelper.getHelper().getWritableDatabase();
		String tableName = getTableName();
		String[] whereArgs = new String[]{String.valueOf(_id)};
		db.delete(tableName, CommonFields.Id + " = ?", whereArgs);
		//db.close();
	}
}
