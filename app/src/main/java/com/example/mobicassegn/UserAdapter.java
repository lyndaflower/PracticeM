package com.example.mobicassegn;

public class UserAdapter {
    private String email;
    private String fullName;
    private String password;
    public UserAdapter() { }
    public UserAdapter(String fullName, String email){
        this.fullName = fullName;
        this.email = email;
    }
    public void setpassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getFullName() {
        return fullName;
    }
    public String getEmail() {
        return email;
    }

}
