package com.example.zhang.objectbox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Relation;
import io.objectbox.relation.ToOne;

/**
 * 由 zhang 于 2017/12/1 创建
 */

@Entity
public class CheckItem {

    @Id
    long id;

    String title;
    ToOne<Task> task;

    public CheckItem(){
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}
