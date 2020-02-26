package com.aliindustries.groceryshoppinglist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.text.DecimalFormat;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "grocerylist.db";
    public static final String TABLE_NAME = "groceryitem_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "title";
    public static final String COL_3 = "item";
    public static final String COL_4 = "ischecked";
    public static final String COL_5 = "quantity";
    public static final String COL_6 = "price";
    public static final String COL_7 = "week";
    public static final String COL_8 = "month";
    public static final String COL_9 = "year";
    public static final String COL_10 = "dateinms";
    public static final String COL_11 = "datecreated";
    DecimalFormat decim = new DecimalFormat("0.00");

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
        db.execSQL("CREATE TABLE " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,ITEM TEXT,ISCHECKED INTEGER, QUANTITY INTEGER,PRICE REAL,WEEK TEXT,MONTH TEXT,YEAR INTEGER,DATEINMS INTEGER,DATECREATED TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title, String item,Integer ischecked,Integer qty, double price,String week, String month, int yr,long datems,String date_created) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,item);
        contentValues.put(COL_4,ischecked);
        contentValues.put(COL_5,qty);
        contentValues.put(COL_6,price);
        contentValues.put(COL_7,week);
        contentValues.put(COL_8,month);
        contentValues.put(COL_9,yr);
        contentValues.put(COL_10,datems);
        contentValues.put(COL_11,date_created);


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

    public Cursor getTitle(String identifier) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+ " where ITEM = '" + identifier +"'",null);
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

    public long getAllCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(*) from " + TABLE_NAME);
        long count = s.simpleQueryForLong();

        return count;
    }

    public long getIsCheckedCount(String title, Integer is_checked,String itemidentifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(ISCHECKED) from " + TABLE_NAME + " where TITLE = '" + title +"' AND ISCHECKED = '" + is_checked +"' AND ITEM != '" + itemidentifier+"'");
        long count = s.simpleQueryForLong();

        return count;
    }
    public long getItemRemainingCount(String week, Integer is_checked,String itemidentifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(ISCHECKED) from " + TABLE_NAME + " where WEEK = '" + week +"' AND ISCHECKED = '" + is_checked +"' AND ITEM != '" + itemidentifier+"'");
        long count = s.simpleQueryForLong();

        return count;
    }
    public Cursor getIsChecked(String title,String item, Integer qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME  + " where TITLE = '" + title+"' AND ITEM = '" + item +"' AND QUANTITY = '" + qty+"'",null);
        return res;
    }
    public Cursor getItemWeekIsChecked(String week,int ischecked, String itemidentifier) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where WEEK = '" + week +"' AND ITEM != '" + itemidentifier+"' AND ISCHECKED == '" + ischecked+"'",null);
        return res;
    }

    public long getItemCount(String title, String itemidentifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(ITEM) from " + TABLE_NAME + " where TITLE = '" + title +"' AND ITEM != '" + itemidentifier+"'");
        long count = s.simpleQueryForLong();

        return count;
    }

    public long getItemWeekIsCheckedCount(String week,int ischecked, String itemidentifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(ITEM) from " + TABLE_NAME + " where WEEK = '" + week +"' AND ITEM != '" + itemidentifier+"' AND ISCHECKED == '" + ischecked+"'");
        long count = s.simpleQueryForLong();

        return count;
    }


    public Cursor getWeekOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT(WEEK), DATEINMS from " + TABLE_NAME + " ORDER BY DATEINMS",null);
        return res;
    }
    public Cursor getMonthOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT(MONTH), DATEINMS from " + TABLE_NAME + " ORDER BY DATEINMS",null);
        return res;
    }
    public Cursor getYrOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT(YEAR), DATEINMS from " + TABLE_NAME + " ORDER BY DATEINMS",null);
        return res;
    }
    public Cursor getMaxID() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select MAX(ID) from " + TABLE_NAME,null);
        return res;
    }
    public String getTotalSumSpent(String week){
        double totalsum =0;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select SUM(QUANTITY * PRICE) from " + TABLE_NAME +  " where WEEK = '" + week +"'",null);
        if(cursor.moveToFirst()) {
            totalsum = cursor.getDouble(0);
        }

        round(totalsum,2);
        String s = decim.format(totalsum);

        return s;
    }
    public String getTotalSumSpent_ischecked(String week,int ischecked){
        double totalsum =0;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select SUM(QUANTITY * PRICE) from " + TABLE_NAME +  " where WEEK = '" + week +"' AND ISCHECKED = '" + ischecked +"'",null);
        if(cursor.moveToFirst()) {
            totalsum = cursor.getDouble(0);
        }

        round(totalsum,2);
        String s = decim.format(totalsum);

        return s;
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
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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

    public boolean updatePrice(String id, double price,Integer is_checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_4,is_checked);
        contentValues.put(COL_6,price);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }







}
