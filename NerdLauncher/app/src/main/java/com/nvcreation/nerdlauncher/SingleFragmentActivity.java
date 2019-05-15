/*
 *
 *  * App Name : Nerd Launcher (Based on Big Nerd Ranch Guide)
 *  * Created by Jinisha Vora | Email: jinishatanna@gmail.com / jinishavora15@gmail.com | Domain: nvcreation.com
 *  * Copyright (c) 2019. All Rights Reserved.
 *  * Last Modified: 5/10/19 2:49 PM
 *
 *
 */

package com.nvcreation.nerdlauncher;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
