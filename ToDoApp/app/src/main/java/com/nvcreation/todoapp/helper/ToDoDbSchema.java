/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/6/19 4:32 PM
 */

package com.nvcreation.todoapp.helper;

/*
 * App name: ToDoApp
 * Purpose: To help make a note of day to day To Do Tasks and check them off once completed.
 * Created by: Jinisha Vora - jinishatanna@gmail.com - 5th March, 2019
 * File Description: Contract Class for Schema declaration - Defines tables and columns
 *
 * */

public class ToDoDbSchema {

    public static final class ToDoTable {

        public static final String TABLE_NAME = "todo";

        public static final class Cols {
            public static final String TODO_ID = "todo_id";
            public static final String NOTE = "note";
            public static final String STATUS = "status";
            public static final String CREATED_AT = "created_at";
            public static final String LAST_MODIFIED = "last_modified";
        }
    }

}
