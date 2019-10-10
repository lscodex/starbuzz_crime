package com.example.lscodex.crimeapps.controller.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.lscodex.crimeapps.model.CrimeDec;

import java.util.Date;
import java.util.UUID;

/**
 *   crimelab cursor wrapper helper for init variables
 */

public class CrimeCursorWrapper extends CursorWrapper{
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    //column datalarını çekmek için kullandığımız method
    public CrimeDec getCrime(){
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT));

        CrimeDec crime = new CrimeDec(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setmSuspect(suspect);
        return crime;
    }
}
