package com.vinay.guessthemovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by salimatti on 1/6/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private  long a=0;

    public DBHelper(Context context) {
        super(context,"MovieName.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table tb_movie(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,Actors TEXT,Director TEXT,Genre TEXT,Language TEXT,Plot TEXT,Poster TEXT,Year TEXT);" );
    //sqLiteDatabase.execSQL("create table tb_employee(id INTEGER PRIMARY KEY AUTOINCREMENT,emp_name TEXT,age INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tb_movie");
        onCreate(sqLiteDatabase);
    }

    public int cleanDb(){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("tb_movie","id>=?",new String[]{"0"});

    }


    public long addMovieDetails(JSONArray jsonArray) {

        SQLiteDatabase db = this.getWritableDatabase();

        //db.delete("tb_movie","id>=?",new String[]{"0"});



        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                //SQLiteDatabase db = this.getWritableDatabase();

               // db.delete("tb_movie","id>=?",new String[]{"0"});

                ContentValues values = new ContentValues();
                values.put("title", jsonObject.getString("title"));
                values.put("Actors", jsonObject.getString("title"));
                values.put("Director", jsonObject.getString("title"));
                values.put("Genre", jsonObject.getString("title"));
                values.put("Language", jsonObject.getString("title"));
                values.put("Plot", jsonObject.getString("title"));
                values.put("Poster", jsonObject.getString("title"));
                values.put("Year", jsonObject.getString("title"));

                // Inserting Row
                a = db.insert("tb_movie", null, values);
               // db.close(); // Closing database connection
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

            db.close();
        return a;
    }



    public ArrayList<HashMap<String,String>> getAllMovieDetails() {
        ArrayList<HashMap<String,String>> alEmpList = new ArrayList<HashMap<String,String>>();
        // Select All Query
        String selectQuery = "SELECT  * FROM tb_movie;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        HashMap<String,String> hm;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                hm=new HashMap<String,String>();
                hm.put("id", ""+cursor.getInt(0));
                hm.put("title", ""+cursor.getString(1));
                hm.put("Actors", ""+cursor.getString(2));
                hm.put("Director", ""+cursor.getString(3));
                hm.put("Genre", ""+cursor.getString(4));
                hm.put("Language", ""+cursor.getString(5));
                hm.put("Plot", ""+cursor.getString(7));
                hm.put("Poster", ""+cursor.getString(6));
                hm.put("Year", ""+cursor.getString(8));
                // Adding emp to list
                alEmpList.add(hm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return contact list
        return alEmpList;
    }
}
