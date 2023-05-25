package com.berkaysenkoylu.imagesharingapp.models;

import java.util.Date;

public class Post {
    public String id;
    public String email;
    public String comment;
    public String imageUrl;
    public Date date;

    public Post(String id, String email, String comment, String imageUrl, Date date) {
        this.id = id;
        this.email = email;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.date = date;
    }
}
