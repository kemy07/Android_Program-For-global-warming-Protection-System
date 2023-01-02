package com.example.gastemphum;

public class User {

    private String Email;
    private String password ;
    private String name;

    public User(String Email, String password, String name) {
        this.Email = Email;
        this.password = password;
        this.name = name;
    }

    public User() {}

    // Getters


    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    // Setters


    public void setEmail(String email) {
        this.Email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
