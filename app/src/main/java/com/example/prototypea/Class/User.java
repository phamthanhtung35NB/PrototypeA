package com.example.prototypea.Class;

public class User {
    public String fullName;
    public String birthYear;
    public String email;

    public String image;

    public User() {
        }

    public User(String fullName, String birthYear, String phone, String image) {
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.email = phone;
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}