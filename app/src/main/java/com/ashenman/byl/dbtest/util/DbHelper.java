package com.ashenman.byl.dbtest.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ashenman.byl.dbtest.util.Dbutil;

/**
 * Created by android on 2016/9/6.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static String dbName = "tee.db";
    private static int dbVersion = 1;

    public static String CREATE_TABLE;

    public DbHelper(Context context, Class c) {
        super(context, dbName, null, dbVersion);
        CREATE_TABLE = Dbutil.creatDbName(c);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

