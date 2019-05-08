package com.nvcreation.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.nvcreation.criminalintent.database.CrimeBaseHelper;
import com.nvcreation.criminalintent.database.CrimeCursorWrapper;
import com.nvcreation.criminalintent.database.CrimeDbSchema;
import com.nvcreation.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.nvcreation.criminalintent.database.CrimeDbSchema.*;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    //private List<Crime> mCrimes; //Commented for Chapter 10 - CrimeLab Challenge Performance, Commented for Chapter 14 Database
    //private Map <UUID, Crime> mCrimes; //Chapter 10 Challenge - CrimeLab Challenge Performance - Commented for Chapter 13

    //Added in Chapter 14
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){

        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }


    //private constructor for singleton
    private CrimeLab(Context context){

        mContext = context.getApplicationContext(); //Added in Chapter 14
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase(); //Added in Chapter 14

        //mCrimes = new ArrayList<>(); //Commented for Chapter 10 Challenge, Commented for Chapter 14 Database
        //mCrimes = new LinkedHashMap<>(); //Chapter 10 Challenge, commented for chapter 13

        /* Commented in Chapter 13
        for (int i =0; i < 10; i++){
            Crime crime = new Crime();
            crime.setmTitle("Crime #" +i);
            crime.setmSolved(i % 2 == 0);
            crime.setmRequiresPolice(i % 3 == 0); // Chapter 8 Challenge 1
            //mCrimes.add(crime); //Commented for Chapter 10 Challenge
            mCrimes.put(crime.getmId(),crime);
        }*/
    }

    public void addCrime(Crime c){

        //mCrimes.add(c); //Commented for Chapter 14 Database
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }
    //Returns the List
    public List<Crime> getCrimes(){

        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }

        return crimes;

        //return mCrimes; //Commented for Chapter 10 Challenge, Commented for Chapter 14 Database
        //return new ArrayList<>(mCrimes.values()); // Chapter 10 Challenge, commented for chapter 13
    }

    //Returns the Crime with the given ID.
    public Crime getCrime(UUID id){
    /*  //Commented for Chapter 10 Challenge //Commented for Chapter 14 Database*/
        /*for(Crime crime: mCrimes){
            if(crime.getmId().equals(id)){
                return crime;
            }
        }*/


        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " =?", new String[] {id.toString()});

        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally{
            cursor.close();
        }

        //return mCrimes.get(id); // Chapter 10 Challenge, commented for chapter 13
    }

    public File getPhotoFile(Crime crime) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }

    public void updateCrime (Crime crime){
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public void removeCrime (Crime crime){
        String uuidString = crime.getmId().toString();

        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " =? ", new String[] {uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CrimeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getmId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeTable.Cols.DATE, crime.getmDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.ismSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getmSuspect());

        return values;
    }
}
