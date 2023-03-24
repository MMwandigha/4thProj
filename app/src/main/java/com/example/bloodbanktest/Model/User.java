package com.example.bloodbanktest.Model;

public class User {
    String bloodgroup, email, id, idnumber, name, profilepictureurl, phonenumber, search, type;

    public User() {
    }

    public User(String bloodgroup, String email, String id, String idnumber, String name, String profilepictureurl, String phonenumber, String search, String type) {
        this.bloodgroup = bloodgroup;
        this.email = email;
        this.id = id;
        this.idnumber = idnumber;
        this.name = name;
        this.profilepictureurl = profilepictureurl;
        this.phonenumber = phonenumber;
        this.search = search;
        this.type = type;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
