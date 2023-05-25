package com.berkaysenkoylu.imagesharingapp.models;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    public String id;
    public String email;
    public String comment;
    public String imageUrl;
    public String userId;
    public Date date;

    public Post(String id, String email, String comment, String imageUrl, String userId, Date date) {
        this.id = id;
        this.email = email;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.date = date;
    }
}
