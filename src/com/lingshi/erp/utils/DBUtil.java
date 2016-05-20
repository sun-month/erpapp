package com.lingshi.erp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBUtil {

	private static DBUtil dbUtil = null;
	private DBHelper helper;
	private SQLiteDatabase db;

	private DBUtil(Context context, String name, int version) {
		helper = new DBHelper(context, name, null, version);
		db = helper.getWritableDatabase();
	}

	/**
	 * @param context
	 *            上下文
	 * @param name
	 *            数据库名称
	 * @param version
	 *            数据库版本
	 */
	public static DBUtil getInstance(Context context, String name, int version) {
		if (dbUtil == null) {
			dbUtil = new DBUtil(context, name, version);
		}
		return dbUtil;
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		return db.insert(table, nullColumnHack, values);
	}

	public long insertOrThrow(String table, String nullColumnHack,
			ContentValues values) throws SQLException {
		return db.insertOrThrow(table, nullColumnHack, values);
	}

	public long insertWithOnConflict(String table, String nullColumnHack,
			ContentValues values, ContentValues initialValues,
			int conflictAlgorithm) {
		return db.insertWithOnConflict(table, nullColumnHack, initialValues,
				conflictAlgorithm);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		return db.delete(table, whereClause, whereArgs);
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		return db.update(table, values, whereClause, whereArgs);
	}

	public int updateWithOnConflict(String table, ContentValues values,
			String whereClause, String[] whereArgs, int conflictAlgorithm) {
		return db.updateWithOnConflict(table, values, whereClause, whereArgs,
				conflictAlgorithm);
	}

	public Map<String, Object> queryForMap(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		List<Map<String, Object>> list = queryForListMap(table, columns,
				selection, selectionArgs, groupBy, having, orderBy, limit);
		return list == null ? null : list.get(0);
	}

	public List<Map<String, Object>> queryForListMap(String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		Cursor cursor = db.query(table, columns, selection, selectionArgs,
				groupBy, having, orderBy, limit);
		return parseCursor(cursor);

	}

	private List<Map<String, Object>> parseCursor(Cursor cursor) {
		if (!cursor.moveToFirst())
			return null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (cursor.moveToFirst()) {
			do {
				Map<String, Object> map = new HashMap<String, Object>();
				String[] columnNames = cursor.getColumnNames();
				for (int i = 0; i < columnNames.length; i++) {
					String key = columnNames[i];
					String value = cursor.getString(cursor.getColumnIndex(key));
					map.put(key, value);
				}
				list.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	/**
	 * 要求values必须要有id_f字段
	 * 
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 */
	public void saveOrUpdate(String table, String nullColumnHack,
			ContentValues values) {
		// 根据id_f查找表中是否存在数据
		Cursor cursor = db.query(table, new String[] { "id_f" }, "id_f=?",
				new String[] { values.get("id_f").toString() }, null, null,
				null);
		// 如果存在，则更新
		if (cursor.getCount() != 0) {
			db.update(table, values, "id_f=?", new String[] { values
					.get("id_f").toString() });
		} else {
			// 如果不存在，则插入
			db.insert(table, nullColumnHack, values);
		}
	}

	public void update(String sql, Object[] objects) {
		db.beginTransaction();
		try {
			db.execSQL(sql, objects);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("DBExecuteException", e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

}
