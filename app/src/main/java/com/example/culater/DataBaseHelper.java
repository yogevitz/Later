package com.example.culater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private static final String TABEL_NAME = "Users";
    private static final String COL_1_1 = "mail";
    private static final String COL_1_2 = "password";
    private static final String COL_1_3 = "points";
    /*
    private static final String TABEL_NAME2 = "Log";
    private static final String COL_2_1 = "seq";
    private static final String COL_2_2 = "mail";
    private static final String COL_2_3 = "start";
    private static final String COL_2_4 = "end";
    private static final String COL_2_5 = "points";
*/
    public DataBaseHelper(Context context) {
        super(context, TABEL_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS "+ TABEL_NAME + "(mail TEXT PRIMARY KEY, " + COL_1_2 + " TEXT, " + COL_1_3 +" INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_NAME);
        onCreate(db);
    }

    public boolean checkEmailAndPassword(String mail, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABEL_NAME+" WHERE "+COL_1_1+" = ?" +"AND "+COL_1_2+" = ?";
        Cursor c = db.rawQuery(query,new String[]{ mail , password});
       // while( c.moveToNext())
        //    System.out.println(c.getString(0) +","+c.getString(1) +","+c.getString(2));

        if(c.getCount()>0)
            return true;
        else return false;
    }

    public int addUser(String mail, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        CheckIfExist(mail,db);
        contentValues.put(COL_1_1,mail);
        contentValues.put(COL_1_2,password);
        contentValues.put(COL_1_3,0);
        long newRowId = db.insert(TABEL_NAME, null, contentValues);
        if(newRowId == -1 )
            return  1; // problem with the adding
        else return 2; // true

    }

    private boolean CheckIfExist(String mail,SQLiteDatabase db) {
        String[] projection = {
                COL_1_1,COL_1_2,COL_1_3
        };

        String selection = COL_1_1 + " = ?";
        String[] selectionArgs = {mail};

        Cursor cursor = db.query(
                TABEL_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order
        );
        System.out.println("check shit");
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABEL_NAME;
        return db.rawQuery(query,null);
    }

    public void updatePoints(int points){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String rawQuery = "UPDATE "+TABEL_NAME+" set "+COL_1_3+ "="+points;
            db.rawQuery(rawQuery, null);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
