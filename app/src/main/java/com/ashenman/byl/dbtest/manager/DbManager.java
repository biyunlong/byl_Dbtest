package com.ashenman.byl.dbtest.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashenman.byl.dbtest.modle.User;
import com.ashenman.byl.dbtest.util.DbHelper;
import com.ashenman.byl.dbtest.util.Dbutil;

import java.util.List;

/**
 * Created by android on 2016/9/9.
 */
public class DbManager<T> {
    private DbHelper helper;
    private SQLiteDatabase db;
    private String tableName;

    public DbManager(Context context, Class c) {
        helper = new DbHelper(context, c);
        tableName=c.getSimpleName()+"db";
        db = helper.getWritableDatabase();
    }


    public void add(User user) {
        ContentValues values = Dbutil.getContentValues(user);
        long insert = db.insert(tableName, null, values);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        return Dbutil.getEntity(cursor, clazz);
    }

    public <T> List<T> findByInt(Class<T> clazz,String cnum,int value) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("select * from  ").
        append(tableName).append(" where ").append(cnum).append("=?");
        Cursor c=db.rawQuery(buffer.toString(),new String []{String.valueOf(value)});
        return Dbutil.getEntity(c, clazz);
    }

    public <T> List<T> findByString(Class<T> clazz,String cnum,String value) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("select * from  ").
                append(tableName).append(" where ").append(cnum).append("=?");
        Cursor c=db.rawQuery(buffer.toString(),new String []{String.valueOf(value)});
        return Dbutil.getEntity(c, clazz);
    }

    public void update(String upContent,String name,String whereContent,Object values){
//        writableDatabase.execSQL("update account set name=?,balance = ? where _id = ?",new Object[]{a.getName(),a.getBalance(),a.getId()});
        ContentValues content=new ContentValues();
        content.put(upContent,name);
        String wheresql=whereContent+" ?";
        int result=db.update(tableName,content,wheresql,new String[]{String.valueOf(values)});
    }

    public void delete(String where,String value){
        db.delete(tableName,where,new String[]{value});
    }
    public void close(){
        db.close();
    }
}
