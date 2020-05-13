package fr.miage.m2.ams.frontoffice.consumingrest;

import java.io.Serializable;

public class Post implements Serializable {

    private int userId;
    private int id;
    private String title;
    private String body;

    public Post(int i, String introduction_to_spring_boot, String s) {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
// getters and setters
}
