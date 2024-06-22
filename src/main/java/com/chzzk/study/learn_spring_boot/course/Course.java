package com.chzzk.study.learn_spring_boot.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Course {

    @Id
    private long id;

    private String name;

    private String author;

    // Constructor
    public Course() {

    }

    public Course(long id, String name, String author) {
        this.name = name;
        this.author = author;
        this.id = id;
    }

    // Setter
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // Getter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    // toString
    @Override
    public String  toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
