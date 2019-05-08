/*
 * Created by: Jinisha Vora - jinishatanna@gmail.com
 * Domain: nvcreation.com
 * Copyright (c) 2019. All rights reserved.
 * Last modified: 3/13/19 5:11 PM
 */

package com.nvcreation.todoapp.model;

/*
* App name: ToDoApp
* Purpose: To help make a note of day to day To Do Tasks and check them off once completed.
* Created by: Jinisha Vora - jinishatanna@gmail.com - 5th March, 2019
* File Description: Model Class for ToDo table (ToDo Table is for saving ToDo notes/text)
*
* */
public class ToDo {

    long id;
    String note;
    int status;
    String createdAt;
    String lastModified;

    public ToDo(long id){
        this.id = id;
    }

    public ToDo(String note, int status) {
        this.note = note;
        this.status = status;
    }

    public ToDo(long id, String note, int status) {
        this.id = id;
        this.note = note;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastModified() {
        return lastModified;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
