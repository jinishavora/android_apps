/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/12/19 3:04 PM
 */

package com.nvcreation.todoapp;

/*
 * App name: ToDoApp
 * Purpose: To help make a note of day to day To Do Tasks and check them off once completed.
 * Created by: Jinisha Vora - jinishatanna@gmail.com - March, 2019
 * File Description: Main Launcher Activity - Calls Fragment with Recycler View to List Todos
 *
 * */


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListToDoFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
