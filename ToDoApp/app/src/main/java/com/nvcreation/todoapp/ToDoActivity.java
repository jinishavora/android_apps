/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/13/19 2:39 PM
 */

package com.nvcreation.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.nvcreation.todoapp.model.ToDo;

public class ToDoActivity extends SingleFragmentActivity {

    private static final String EXTRA_TODO_ID = "activity_todo_id";

    public static Intent newIntent(Context packageContext, long todo_id){
        Intent intent = new Intent(packageContext, ToDoActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todo_id);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        long todo_id = (long) getIntent().getSerializableExtra(EXTRA_TODO_ID);
        return ToDoFragment.newInstance(todo_id);
    }


}
