package com.example.finalprojectnetwork.modle;

public class User {
    private String name, major;
    private String email;
    private boolean isPublic;

    public User() {
    }

    public User(String name, String major, String email, boolean isPublic) {
        this.name = name;
        this.major = major;
        this.email = email;
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
