package com.example.finalprojectnetwork.modle;

public class Image {
    private String title;
    private String url;
    private boolean isFavorite;
    private String key;

    public Image(String title, String url) {
        if (title.trim().equals("")){
            title="No Name";
        }
        this.title = title;
        this.url = url;
    }

    public Image() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Image{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
