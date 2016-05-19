package com.lingshi.erp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String CREATE_USER = "create table t_user (id integer primary key autoincrement,id_f integer,name_f text,code_f text,password_f text)";

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			String alter_createtime = "alter table t_user add column createtime_f real";// 增加字段createtime
			db.execSQL(alter_createtime);
			String alter_isremember = "alter table t_user add column isremember_f integer ";// 增加字段是否记住密码
			db.execSQL(alter_isremember);

		default:
			break;
		}
	}

}
