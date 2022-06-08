package com.project.lovedatingapp.models;

public class Post {
    private String PostID;
    private String ImageLink;
    private String Caption;
    private String Publisher;

    public Post() {
    }

    public Post(String postID, String imageLink, String caption, String publisher) {
        PostID = postID;
        ImageLink = imageLink;
        Caption = caption;
        Publisher = publisher;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }
}
