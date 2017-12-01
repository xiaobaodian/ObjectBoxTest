package com.example.zhang.objectbox;

import java.util.Date;
import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Relation;
import io.objectbox.relation.ToMany;

@Entity
public class Task {

    @Id
    long id;

    String title;
    String comment;
    Date date;

    //@Backlink
    ToMany<CheckItem> checkList;

    public Task(long id, String title, String comment, Date date) {

        this.id = id;
        this.title = title;
        this.comment = comment;
        this.date = date;
    }

    public Task() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}