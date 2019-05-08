/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/13/19 4:30 PM
 */

package com.nvcreation.todoapp;

/*
 * App name: ToDoApp
 * Purpose: To help make a note of day to day To Do Tasks and check them off once completed.
 * Created by: Jinisha Vora - jinishatanna@gmail.com - March, 2019
 * File Description: Fragment with Recycler View to List Todos, Menu Options
 *
 * */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.nvcreation.todoapp.helper.ToDoDatabaseHelper;
import com.nvcreation.todoapp.model.ToDo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListToDoFragment extends Fragment {

    private RecyclerView mToDoRecyclerView;

    private List<ToDo> mTodo;

    private ToDoAdapter todoAdapter;

    private Context mContext;

    private ToDoDatabaseHelper toDoDatabaseHelper;

    private int mClickedToDoItemPosition;

    private static final int REQUEST_TODO = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mToDoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);
        mToDoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mTodo = new ToDoDatabaseHelper(mContext).getAllToDo();
        todoAdapter = new ToDoAdapter(mTodo);
        mToDoRecyclerView.setAdapter(todoAdapter);

        todoAdapter.notifyDataSetChanged();
        return view;
    }


    //Menu Code starts

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_topright, menu);

        MenuItem addToDo = menu.findItem(R.id.add_todo);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_todo ){

            long todo_id = new ToDoDatabaseHelper(getActivity()).addToDo();
            Intent intent = ToDoActivity.newIntent(getActivity(), todo_id);
            startActivity(intent);
            /*Intent intent = new Intent(getActivity(), ToDoActivity.class);
            startActivityForResult(intent, REQUEST_TODO);*/
        }

        return true;
    }


    //Menu Code ends

    // Recycler View Holder

    private class ToDoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mToDoTextView;
        private CheckBox mMarkCheckBox;
        private ToDo mToDo;

        public ToDoHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Toast.makeText(getActivity(), "Delete on Long Click", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                    alertDialog.setTitle("Confirm Deletion");
                    alertDialog.setMessage("Do you want to Delete this Todo?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new ToDoDatabaseHelper(getActivity()).deleteToDo(mToDo);
                            todoAdapter.delete(getAdapterPosition());
                        }
                    });
                    alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDelete = alertDialog.create();
                    alertDelete.show();

                    return true;
                }
            });

            mToDoTextView = (TextView) itemView.findViewById(R.id.tv_todo);
            mMarkCheckBox = (CheckBox) itemView.findViewById(R.id.cb_check);

            mMarkCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(getStatus(mToDo)){
                        mMarkCheckBox.setChecked(false);
                        mToDo.setStatus(0);
                        new ToDoDatabaseHelper(getActivity()).updateToDo(mToDo);
                    }else{
                        mMarkCheckBox.setChecked(true);
                        mToDo.setStatus(1);
                        new ToDoDatabaseHelper(getActivity()).updateToDo(mToDo);
                    }
                }
            });

        }

        public void bind(ToDo todo){

            mToDo = todo;
            if(mToDo.getNote().length() > 20){

                String[] words = mToDo.getNote().split("\\s+");
                String sentence = "";
                if(words.length > 10){
                    for(int i=0; i < 10; i++){
                        sentence = sentence + words[i] + " ";
                        mToDoTextView.setText(sentence);
                    }
                }
                else{
                    mToDoTextView.setText(mToDo.getNote());
                }

            }else{
                mToDoTextView.setText(mToDo.getNote());
            }

            mMarkCheckBox.setChecked(getStatus(mToDo));

        }

        @Override
        public void onClick(View v) {

            mClickedToDoItemPosition = getAdapterPosition();

            Intent intent = ToDoActivity.newIntent(getActivity(), mToDo.getId());
            startActivity(intent);

        }



        public boolean getStatus(ToDo todo){

            mToDo = todo;
            if(mToDo.getStatus() == 0){
                return false;
            }
            else {
                return true;
            }

        }


    }

    // Recycler View Adapter

    private class ToDoAdapter extends RecyclerView.Adapter<ToDoHolder>{

        private List<ToDo> mTodos;

        public ToDoAdapter(List<ToDo> todos){
            mTodos = todos;
        }

        @NonNull
        @Override
        public ToDoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.list_todo, viewGroup, false);
            return new ToDoHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoHolder toDoHolder, int i) {

            ToDo todo = mTodos.get(i);
            toDoHolder.bind(todo);
        }

        @Override
        public int getItemCount() {
                return mTodos.size();

        }

        public void delete(int position){
            mTodos.remove(position);
            notifyItemRemoved(position);
        }
    }

}
