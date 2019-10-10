package com.example.lscodex.crimeapps.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lscodex.crimeapps.controller.Database.CrimeBaseHelper;
import com.example.lscodex.crimeapps.controller.Database.CrimeCursorWrapper;
import com.example.lscodex.crimeapps.controller.Database.CrimeDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CrimeLab {



    private static final String TAG = CrimeLab.class.getName();

    private static CrimeLab sCrimeLab;

    // 1- create database

    private Context mContext;
    private static SQLiteDatabase mDatabase;


    //constructor and init
    private CrimeLab(Context c) {
        mContext = c.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase(); // write and read

    }

    // added columns
    private static ContentValues getContentValues(CrimeDec crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getmSuspect());
        return values;
    }

    // set the lab if null
    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c);

        }
        return sCrimeLab;
    }


    // create wrapper cursor
    public List<CrimeDec> getCrimeList() {
        List<CrimeDec> listCrime = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst(); // first move
            while (!cursor.isAfterLast()) {// end values
                listCrime.add(cursor.getCrime());
                cursor.moveToNext();// next
            }
        } finally {//cursor shutdown
            cursor.close();
        }
        return listCrime;// return list
    }

    //UUID id sini geri getirmek için
    public CrimeDec getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        Log.d(TAG, "CURSOR: "+ cursor.toString());
        try {
            if (cursor.getCount() == 0) {
                Log.d(TAG, "Cursor getCount is 0 : ");
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    // added crime
    public void addCrime(CrimeDec addcrime) {
        ContentValues addValues = getContentValues(addcrime);

        Log.d("UUID ----> ", "AddValues --> : " + addValues);
        mDatabase.insert(CrimeDbSchema.CrimeTable.TABLE_NAME, null, addValues);
    }

    // delete crime
    public void deleteCrime(CrimeDec crime) {
        ContentValues aaa = getContentValues(crime);
        Log.d("ContenValuess", "ContenValuse -----> " + aaa);
        mDatabase.delete(CrimeDbSchema.CrimeTable.TABLE_NAME, CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{crime.getId().toString()});
    }
    //delete for UUID 's

    public void deleteUUID(UUID crimeId){
        mDatabase.delete(CrimeDbSchema.CrimeTable.TABLE_NAME,CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",new String[]{crimeId.toString()});
        Log.d(TAG, "deleteUUID: "  + mDatabase.delete(CrimeDbSchema.CrimeTable.TABLE_NAME,CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",new String[]{crimeId.toString()}));
    }


    // upload information crime
    public void updateCrime(CrimeDec upCrime) {
        String uuidString = upCrime.getId().toString();
        ContentValues upValues = getContentValues(upCrime);

        mDatabase.update(CrimeDbSchema.CrimeTable.TABLE_NAME, upValues, CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});

    }

    // database cursorwrapper query
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeDbSchema.CrimeTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

    // get the photo
    public File getPhotoFile(CrimeDec crimedec){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir,crimedec.getPhotoFilename());
    }


}
