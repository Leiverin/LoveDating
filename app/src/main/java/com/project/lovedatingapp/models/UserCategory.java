package com.project.lovedatingapp.models;

import java.util.List;

public class UserCategory {
    private User user;
    private List<Image> images;

    public UserCategory(User user, List<Image> images) {
        this.user = user;
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
