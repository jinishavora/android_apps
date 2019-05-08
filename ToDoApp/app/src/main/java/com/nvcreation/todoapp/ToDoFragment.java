/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/14/19 9:40 AM
 */

package com.nvcreation.todoapp;

/*
 * App name: ToDoApp
 * Purpose: To help make a note of day to day To Do Tasks and check them off once completed.
 * Created by: Jinisha Vora - jinishatanna@gmail.com - March, 2019
 * File Description: Fragment - To create/edit todo and tags using save button
 *
 * */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nvcreation.todoapp.helper.ToDoDatabaseHelper;
import com.nvcreation.todoapp.model.ToDo;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class ToDoFragment extends Fragment {


    private EditText mAddToDoEditText;
    private TextView mDateTimeTextView;
    private Button mSaveToDoButton;

    public static final String ARG_TODO_ID = "fragment_todo_id";

    ToDoDatabaseHelper mToDoDatabaseHelper;
    ToDo mToDo;


    public static ToDoFragment newInstance(long todo_id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TODO_ID, todo_id);

        ToDoFragment fragment = new ToDoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long todo_id = (long) getArguments().getSerializable(ARG_TODO_ID);
        mToDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
        mToDo = mToDoDatabaseHelper.getToDo(todo_id);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        mAddToDoEditText = view.findViewById(R.id.et_add_todo_text);
        mDateTimeTextView = view.findViewById(R.id.tv_date_time);
        mSaveToDoButton = view.findViewById(R.id.btn_save);

        mAddToDoEditText.setText(mToDo.getNote());
        mDateTimeTextView.setText(mToDo.getLastModified());

        mSaveToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mToDo.setNote(mAddToDoEditText.getText().toString());

                mToDoDatabaseHelper.updateToDo(mToDo);

            }
        });


        return view;

    }
}
