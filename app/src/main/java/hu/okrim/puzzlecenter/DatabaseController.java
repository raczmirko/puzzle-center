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

    public boolean addRecord(DataEntryModel dataEntryModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TIME, dataEntryModel.getSolveTime());
        cv.put(COLUMN_PUZZLE, dataEntryModel.getPuzzle());
        cv.put(COLUMN_DATE, dataEntryModel.getDateString());

        long insert = db.insert(RECORDS_TABLE, null, cv);

        return insert == -1;
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

                DataEntryModel newCustomer = new DataEntryModel(dateString, puzzle, time);
                returnList.add(newCustomer);

            }while(cursor.moveToNext());
        }else{
            //Failure, do not add anything to the list.
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
        }else{
            //failure, do not add anything to the list
        }

        //close both cursor and connection
        cursor.close();
        db.close();
        return returnList;
    }

    public List<DataEntryModel> getTop3Solves(String puzzleToCheck){
        List<DataEntryModel> returnList = new ArrayList<>();
        //Get data from the Database.
        String queryString = "SELECT COLUMN_TIME, COUNT(COLUMN_TIME) AS times, MAX(COLUMN_PUZZLE), MAX(COLUMN_DATE) FROM " + RECORDS_TABLE + " WHERE COLUMN_PUZZLE = " + "'" + puzzleToCheck + "'" + " GROUP BY COLUMN_TIME ORDER BY COLUMN_TIME ASC LIMIT 3";
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
        }else{
            //failure, do not add anything to the list
        }

        //close both cursor and connection
        cursor.close();
        db.close();
        return returnList;
    }
}
