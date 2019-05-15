/*
 *
 *  * App Name : Nerd Launcher (Based on Big Nerd Ranch Guide)
 *  * Created by Jinisha Vora | Email: jinishatanna@gmail.com / jinishavora15@gmail.com | Domain: nvcreation.com
 *  * Copyright (c) 2019. All Rights Reserved.
 *  * Last Modified: 5/8/19 6:06 PM
 *
 *
 */

package com.nvcreation.nerdlauncher;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
