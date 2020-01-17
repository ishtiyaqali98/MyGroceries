package com.aliindustries.groceryshoppinglist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "grocerylist.db";
    public static final String TABLE_NAME = "groceryitem_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "title";
    public static final String COL_3 = "item";
    public static final String COL_4 = "ischecked";
    public static final String COL_5 = "quantity";

    private static DatabaseHelper mInstance = null;


    private static final int DATABASE_VERSION = 1;


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static DatabaseHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,ITEM TEXT,ISCHECKED INTEGER, QUANTITY INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title, String item,Integer ischecked,Integer qty) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,item);
        contentValues.put(COL_4,ischecked);
        contentValues.put(COL_5,qty);


        long result = db.insert(TABLE_NAME,null ,contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }



    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getTitle() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT(TITLE) from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getItem(String title, String itemidentifier) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ITEM from "+TABLE_NAME  + " where TITLE = '" + title +"' AND ITEM != '" + itemidentifier+"'",null);
        return res;
    }

    public Cursor getQty_ID(String title, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME  + " where TITLE = '" + title +"' AND ITEM = '" + item+"'",null);
        return res;
    }

    public Cursor getAllTitleRows(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME  + " where TITLE = '" + title+"'",null);
        return res;
    }


    public long getTitleCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(DISTINCT TITLE) from " + TABLE_NAME);
        long count = s.simpleQueryForLong();

        return count;
    }

    public long getIsCheckedCount(String title, Integer is_checked,String itemidentifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(ISCHECKED) from " + TABLE_NAME + " where TITLE = '" + title +"' AND ISCHECKED = '" + is_checked +"' AND ITEM != '" + itemidentifier+"'");
        long count = s.simpleQueryForLong();

        return count;
    }

    public Cursor getIsChecked(String title,String item, Integer qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME  + " where TITLE = '" + title+"' AND ITEM = '" + item +"' AND QUANTITY = '" + qty+"'",null);
        return res;
    }


    public long getItemCount(String title, String itemidentifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(ITEM) from " + TABLE_NAME + " where TITLE = '" + title +"' AND ITEM != '" + itemidentifier+"'");
        long count = s.simpleQueryForLong();

        return count;
    }

    public boolean itemExists(String title, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select ITEM from "+TABLE_NAME + " where TITLE = '" + title +"' AND ITEM = '" + item +"'",null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public boolean titleExists(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select TITLE from "+TABLE_NAME + " where TITLE = '" + title +"'",null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



    public Cursor searchID(String d, String m, String y, String event, String hr, String min, String end_d, String end_m, String end_y, String end_hr, String end_min) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID from "+TABLE_NAME + " where DAY = '" + d +"' AND MONTH = '" + m +"' AND YEAR = '" + y+"' AND EVENT = '" + event +"' AND HOUR = '" + hr+"' AND MINUTE = '" + min+"' AND ENDDAY = '" +end_d+"' AND ENDMONTH = '" +end_m+"' AND ENDYEAR = '" + end_y+"' AND ENDHOUR = '" + end_hr+"' AND ENDMINUTE = '" + end_min+"'",null);
        return res;
    }



    public boolean updateData(String id, String title, String item,Integer ischecked,Integer qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,item);
        contentValues.put(COL_4,ischecked);
        contentValues.put(COL_5,qty);

        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public boolean updateTitle(String id, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,title);

        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }
    public boolean updateIsChecked(String id, Integer is_checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_4,is_checked);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }



    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }







}
