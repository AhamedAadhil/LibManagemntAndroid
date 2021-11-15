package com.example.librarymanagementapp;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String name,email,password,imguri,role;


    public UserModel(){

    }

    public UserModel(String name, String email, String password, String imguri, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imguri = imguri;
        this.role=role;
    }

    protected UserModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        imguri = in.readString();
        role = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(imguri);
        parcel.writeString(role);
    }
}
