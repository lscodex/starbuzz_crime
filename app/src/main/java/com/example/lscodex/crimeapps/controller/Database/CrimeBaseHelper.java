package com.example.lscodex.crimeapps.controller.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lscodex on 2.08.2017.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSIONS = 1 ;
    private static final String DATABASE_NAME = "crimeBase.db";

    // create name database
    public CrimeBaseHelper(Context context){
   super(context,DATABASE_NAME,null,VERSIONS);





    }

    // table columns init
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "create table " + CrimeDbSchema.CrimeTable.TABLE_NAME + "( " +
                " _id integer primary key autoincrement, " +
                CrimeDbSchema.CrimeTable.Cols.UUID + ", " +
                CrimeDbSchema.CrimeTable.Cols.TITLE + ", " +
                CrimeDbSchema.CrimeTable.Cols.DATE + ", " +
                CrimeDbSchema.CrimeTable.Cols.SOLVED + ", "+
                CrimeDbSchema.CrimeTable.Cols.SUSPECT + ")" ;

                db.execSQL(sqlQuery);




    }

    // update table is existss
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
