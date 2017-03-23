package org.smk.model;

import java.io.Serializable;

/**
 * Created by SMK on 3/5/2016.
 */
public class MutipleAnswer implements Serializable {
    private int id;
    private String answer;

    public MutipleAnswer(int id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\""+
                ", \"answer\":\"" + answer + "\"" +
                '}';
    }
}
