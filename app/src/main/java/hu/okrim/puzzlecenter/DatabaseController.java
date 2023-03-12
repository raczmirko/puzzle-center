package hu.okrim.puzzlecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_DATE = "COLUMN_DATE";
    public static final String COLUMN_PUZZLE = "COLUMN_PUZZLE";
    public static final String COLUMN_TIME = "COLUMN_TIME";
    public static final String RECORDS_TABLE = "RECORDS_TABLE";

    public DatabaseController(@Nullable Context context) {
        super(context, "solves.db", null, 1);
    }
    //This is called the first time a database is accessed.
    //Here we have code to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + RECORDS_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_PUZZLE + " TEXT," +
                COLUMN_TIME + " INTEGER" +
                ")";
        db.execSQL(createTableStatement);
    }
    //This is called if the database version is changed.
    //It prevents user apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Since we only run a local database this function is emitted...
    }

    public void addRecord(DataEntryModel dataEntryModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TIME, dataEntryModel.getSolveTime());
        cv.put(COLUMN_PUZZLE, dataEntryModel.getPuzzle());
        cv.put(COLUMN_DATE, dataEntryModel.getDateString());

        long insert = db.insert(RECORDS_TABLE, null, cv);
    }

    public List<DataEntryModel> getAllRecords(){
        List<DataEntryModel> returnList = new ArrayList<>();
        //Get data from the Database.
        String queryString = "SELECT * FROM " + RECORDS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        //If first element was found, meaning there were results...
        if(cursor.moveToFirst()){
            //loop through cursor (result set) and create new customer objects, put them in returnList
            do{
                String dateString = cursor.getString(1);
                String puzzle = cursor.getString(2);
                int time = cursor.getInt(3);

                DataEntryModel newRecord = new DataEntryModel(dateString, puzzle, time);
                returnList.add(newRecord);

            }while(cursor.moveToNext());
        }

        //Close both cursor and connection.
        cursor.close();
        db.close();
        return returnList;
    }

    public List<DataEntryModel> getRecordsForPuzzle(String puzzleToCheck){
        List<DataEntryModel> returnList = new ArrayList<>();
        //Get data from the Database.
        String queryString = "SELECT * FROM " + RECORDS_TABLE + " WHERE COLUMN_PUZZLE = '" + puzzleToCheck + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        //If first element was found, meaning there were results...
        if(cursor.moveToFirst()){
            //loop through cursor (result set) and create new customer objects, put them in returnList
            do{
                String dateString = cursor.getString(1);
                String puzzle = cursor.getString(2);
                int time = cursor.getInt(3);

                DataEntryModel newCustomer = new DataEntryModel(dateString,puzzle,time);
                returnList.add(newCustomer);

            }while(cursor.moveToNext());
        }

        //close both cursor and connection
        cursor.close();
        db.close();
        return returnList;
    }

    public List<DataEntryModel> getTop3Solves(String puzzleToCheck){
        List<DataEntryModel> returnList = new ArrayList<>();
        //Get data from the Database.
        String queryString = "SELECT COLUMN_TIME, COUNT(COLUMN_TIME) AS times, MAX(COLUMN_PUZZLE), MAX(COLUMN_DATE) FROM " + RECORDS_TABLE + " WHERE COLUMN_PUZZLE LIKE(" + "'" + puzzleToCheck + "'" + ") GROUP BY COLUMN_TIME ORDER BY COLUMN_TIME ASC LIMIT 3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        //If first element was found, meaning there were results...
        if(cursor.moveToFirst()){
            //loop through cursor (result set) and create new customer objects, put them in returnList
            do{
                int time = cursor.getInt(0);
                String puzzle = cursor.getString(2);
                String dateString = cursor.getString(3);

                DataEntryModel newCustomer = new DataEntryModel(dateString, puzzle, time);
                returnList.add(newCustomer);

            }while(cursor.moveToNext());
        }

        //close both cursor and connection
        cursor.close();
        db.close();
        return returnList;
    }

    public String getTotalSolves(){
        //Get data from the Database.
        String queryString = "SELECT COUNT(*) FROM " + RECORDS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        String returnString = "";
        //If first element was found, meaning there were results...
        if(cursor.moveToFirst()){
            //loop through cursor (result set) and create new customer objects, put them in returnList
            do{
                returnString = String.valueOf(cursor.getInt(0));
            }while(cursor.moveToNext());
        }
        //close both cursor and connection
        cursor.close();
        db.close();
        return returnString;
    }

    //Gets the solve that has the earliest date
    public List<DataEntryModel> getFirstSolve(){
        List<DataEntryModel> returnList = new ArrayList<>();
        //Get data from the Database.
        String queryString = "SELECT * FROM RECORDS_TABLE WHERE COLUMN_DATE = (SELECT MIN(COLUMN_DATE) FROM RECORDS_TABLE)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        //If first element was found, meaning there were results...
        if(cursor.moveToFirst()){
            //loop through cursor (result set) and create new customer objects, put them in returnList
            do{
                String dateString = cursor.getString(1);
                String puzzle = cursor.getString(2);
                int time = cursor.getInt(3);

                DataEntryModel newCustomer = new DataEntryModel(dateString, puzzle, time);
                returnList.add(newCustomer);

            }while(cursor.moveToNext());
        }

        //close both cursor and connection
        cursor.close();
        db.close();
        return returnList;
    }

    public List<String> getFavouritePuzzle(){
        List<String> returnList = new ArrayList<>();
        //Get data from the Database.
        String queryString = "SELECT COLUMN_PUZZLE, COUNT(1) AS TIMES FROM RECORDS_TABLE GROUP BY COLUMN_PUZZLE ORDER BY TIMES DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        //If first element was found, meaning there were results...
        if(cursor.moveToFirst()){
            //loop through cursor (result set) and create new customer objects, put them in returnList
            do{
                String puzzle = cursor.getString(0);
                String times = cursor.getString(1);

                returnList.add(puzzle);
                returnList.add(times);
            }while(cursor.moveToNext());
        }
        //close both cursor and connection
        cursor.close();
        db.close();
        return returnList;
    }
}
