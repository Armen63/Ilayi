package com.example.goro.quiztest.db.model;

/**
 * Created by Goro on 05.10.2017.
 */

public class Note {

    private int id;
    private String russian;
    private String english;


    public Note(String russian, String english) {
        super();
        this.setRussian(russian);
        this.setEnglish(english);
    }

    @Override
    public String toString() {
        return "Book [id=" + getId() + ", title=" + getRussian() + ", author=" + getEnglish()
                + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRussian() {
        return russian;
    }

    public void setRussian(String russian) {
        this.russian = russian;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}