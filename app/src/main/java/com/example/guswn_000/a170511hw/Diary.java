package com.example.guswn_000.a170511hw;

/**
 * Created by guswn_000 on 2017-05-11.
 */

public class Diary
{
    private String title;
    private String content;

    public Diary(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
