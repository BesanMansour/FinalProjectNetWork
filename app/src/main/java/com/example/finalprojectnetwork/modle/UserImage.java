package com.example.finalprojectnetwork.modle;

public class UserImage {
    private String imageUrl;
    private byte[] image;

    public UserImage() {}

    public UserImage(String imageUrl, byte[] image) {
        this.imageUrl = imageUrl;
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public byte[] getImage() {
        return image;
    }
}

