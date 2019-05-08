/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/13/19 5:13 PM
 */

package com.nvcreation.todoapp.helper;

/*
 * App name: ToDoApp
 * Purpose: To help make a note of day to day To Do Tasks and check them off once completed.
 * Created by: Jinisha Vora - jinishatanna@gmail.com - 5th March, 2019
 * File Description: Database Helper Class for Creating and Maintaining database and tables and for CRUD Operations
 *
 * */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nvcreation.todoapp.R;
import com.nvcreation.todoapp.model.ToDo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.nvcreation.todoapp.helper.ToDoDbSchema.*;

public class ToDoDatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "ToDo Database Helper";

    //Database details: name and version
    private static final String DATABASE_NAME = "ToDoAppDb.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    public ToDoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static final String CREATE_TABLE_TODO = "CREATE TABLE " + ToDoTable.TABLE_NAME +
            "(" + ToDoTable.Cols.TODO_ID + " INTEGER PRIMARY KEY," +
            ToDoTable.Cols.NOTE + " TEXT NOT NULL," +
            ToDoTable.Cols.STATUS + " INTEGER," +
            ToDoTable.Cols.CREATED_AT + " DATETIME," +
            ToDoTable.Cols.LAST_MODIFIED + " DATETIME" +")";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_TODO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ToDoTable.TABLE_NAME);

        onCreate(db);
    }

    //Crud Operations starts


    //CREATE - creating todos, creating tags and creating todo_tag

    public long addToDo(){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoTable.Cols.NOTE, mContext.getString(R.string.blank_default_ToDoText));
        values.put(ToDoTable.Cols.STATUS, 0);
        values.put(ToDoTable.Cols.CREATED_AT, getDateTime());
        values.put(ToDoTable.Cols.LAST_MODIFIED, getDateTime());

        long todo_id = db.insert(ToDoTable.TABLE_NAME, null, values);

        return todo_id;
    }

    public void updateToDo(ToDo toDo){

        SQLiteDatabase db = this.getWritableDatabase();

        long todo_id = toDo.getId();
        ContentValues values = getContentValues(toDo);
        db.update(ToDoTable.TABLE_NAME, values, ToDoTable.Cols.TODO_ID + " =? ", new String[] {String.valueOf(todo_id)} );


    }

    public void deleteToDo(ToDo toDo){

        SQLiteDatabase db = this.getWritableDatabase();

        long todo_id = toDo.getId();
        ContentValues values = getContentValues(toDo);
        db.delete(ToDoTable.TABLE_NAME, ToDoTable.Cols.TODO_ID + " =? ", new String[] {String.valueOf(todo_id)} );


    }

    /*public void createToDo(ToDo todo, long[] tag_ids){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoTable.Cols.NOTE, todo.getNote());
        values.put(ToDoTable.Cols.STATUS, todo.getStatus());
        values.put(ToDoTable.Cols.CREATED_AT, getDateTime());
        values.put(ToDoTable.Cols.LAST_MODIFIED, getDateTime());

        long todo_id = db.insert(ToDoTable.TABLE_NAME, null, values);

        for (long tag_id : tag_ids){
            createToDoTag(todo_id, tag_id);
        }


    }
*/
    //READ

    public List<ToDo> getAllToDo(){

        List<ToDo> todos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + ToDoTable.TABLE_NAME;

        Cursor c = db.rawQuery(query, null);

        if(c != null){
            c.moveToFirst();
            while(!c.isAfterLast()) {
                ToDo todo = new ToDo(c.getLong(c.getColumnIndex(ToDoTable.Cols.TODO_ID)));
                todo.setNote(c.getString(c.getColumnIndex(ToDoTable.Cols.NOTE)));
                todo.setStatus(c.getInt(c.getColumnIndex(ToDoTable.Cols.STATUS)));
                todo.setLastModified(c.getString(c.getColumnIndex(ToDoTable.Cols.LAST_MODIFIED)));
                todos.add(todo);
                c.moveToNext();
            }
        }

        c.close();
        return todos;
    }

    public ToDo getToDo(long todo_id){

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + ToDoTable.TABLE_NAME + " WHERE " + ToDoTable.Cols.TODO_ID + " = " + todo_id;

        Cursor c = db.rawQuery(query, null);

        if(c != null){
            c.moveToFirst();
        }

        ToDo todo = new ToDo(todo_id);
        todo.setNote(c.getString(c.getColumnIndex(ToDoTable.Cols.NOTE)));
        todo.setStatus(c.getInt(c.getColumnIndex(ToDoTable.Cols.STATUS)));
        todo.setLastModified(c.getString(c.getColumnIndex(ToDoTable.Cols.LAST_MODIFIED)));
        c.close();
        return todo;

    }


    private ContentValues getContentValues(ToDo toDo){
        ContentValues values = new ContentValues();
        values.put(ToDoTable.Cols.NOTE, toDo.getNote());
        values.put(ToDoTable.Cols.STATUS, toDo.getStatus());
        values.put(ToDoTable.Cols.LAST_MODIFIED, getDateTime());
        return values;

    }
    private String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

    }

}
