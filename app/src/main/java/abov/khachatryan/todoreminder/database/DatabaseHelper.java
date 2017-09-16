package abov.khachatryan.todoreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import abov.khachatryan.todoreminder.models.ToDo;

/* Created by abov on 9/15/17.*/

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "todoDB";
    private static final String TABLE_NOTES = "todos";

    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_TIME_MILLIS = "millis";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_TIME_MILLIS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }


    public void addToDo(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, toDo.getmDescription());
        values.put(KEY_DATE, toDo.getmDate());
        values.put(KEY_TIME, toDo.getmTime());
        values.put(KEY_TIME_MILLIS, toDo.getmTimeInMillis());
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public List<ToDo> getAllToDos() {
        List<ToDo> toDoList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setmId(Integer.parseInt(cursor.getString(0)));
                toDo.setmDescription(cursor.getString(1));
                toDo.setmDate(cursor.getString(2));
                toDo.setmTime(cursor.getString(3));
                toDo.setmTimeInMillis(cursor.getString(4));
                toDoList.add(toDo);
            } while (cursor.moveToNext());
        }
        return toDoList;
    }

    public void deleteToDo(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[] { String.valueOf(toDo.getmId()) });
        db.close();
    }

    public List<ToDo> getAllOutdatedToDos(){
        List<ToDo> outdatedToDos = new ArrayList<>();
        for (ToDo toDo : getAllToDos()) {
            if(toDo.isOutdated()){
                outdatedToDos.add(toDo);
            }
        }
        return outdatedToDos;
    }

    public void removeOutdatedToDos() {
        for (ToDo toDo : getAllOutdatedToDos()) {
            deleteToDo(toDo);
        }
    }
}
