package com.gobara.musicplayerapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static String DB_NAME="com.gobara.loginwithsqlitedb";
    public static int DB_VERSION=1;
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //create new table to dataBase DB_NAME
        sqLiteDatabase.execSQL("CREATE TABLE loginData(username TEXT PRIMARY KEY ,PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS loginData");
    }
    public boolean inseartData(String username,String Password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues con=new ContentValues();
        con.put("username",username);
        con.put("PASSWORD",Password);
        long result=db.insert("loginData",null,con);
        if(result==-1)
            return false;
        return true;
    }
    public boolean checkUserName(String username){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from loginData where username=?",new String[]{username});
        if(cursor.getCount()>0)
            return true ;
        return false;
    }

    public boolean checkUserNamepassword(String username,String password){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from loginData where username=? and PASSWORD=?",new String[]{username,password});
        if(cursor.getCount()>0)
            return true ;
        return false;
    }
}

