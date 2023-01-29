package com.example.finalprojectnetwork.modle;

public class Summary {
    private String text;
    private String userId;
    private String Image;
//    private boolean isFavorite;

    public Summary(){
    }

    public Summary( String text , String userId , String image ) {
        if (text.trim().equals("")){
            text="No Name";
        }
        this.text = text;
        this.userId = userId;
        Image = image;
    }

    public String getImage( ) {
        return Image;
    }

    public void setImage( String image ) {
        Image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public boolean isFavorite() {
//        return isFavorite;
//    }
//
//    public void setFavorite(boolean favorite) {
//        isFavorite = favorite;
//    }
}
