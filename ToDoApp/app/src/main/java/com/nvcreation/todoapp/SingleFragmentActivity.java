/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/8/19 2:27 PM
 */

package com.nvcreation.todoapp;

/*
 * App name: ToDoApp
 * Purpose: To help make a note of day to day To Do Tasks and check them off once completed.
 * Created by: Jinisha Vora - jinishatanna@gmail.com - March, 2019
 * File Description: Abstract class To inflate the fragment view and instantiate fragment using createFragment() (abstract method)
 *
 * */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,createFragment())
                    .commit();
        }
    }
}
