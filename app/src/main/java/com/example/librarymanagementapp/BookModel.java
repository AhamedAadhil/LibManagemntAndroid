package com.example.librarymanagementapp;

import android.os.Parcel;
import android.os.Parcelable;

public class BookModel implements Parcelable {

    private String bookname, authername, contributors, publisher, edition, ISBN, category, subject, copies, tag, imguri, bookid;

    public BookModel() {

    }

    public BookModel(String bookname, String authername, String contributors, String publisher, String edition, String ISBN, String category, String subject, String copies, String tag, String imguri, String bookid) {
        this.bookname = bookname;
        this.authername = authername;
        this.contributors = contributors;
        this.publisher = publisher;
        this.edition = edition;
        this.ISBN = ISBN;
        this.category = category;
        this.subject = subject;
        this.copies = copies;
        this.tag = tag;
        this.imguri = imguri;
        this.bookid = bookid;
    }

    protected BookModel(Parcel in) {
        bookname = in.readString();
        authername = in.readString();
        contributors = in.readString();
        publisher = in.readString();
        edition = in.readString();
        ISBN = in.readString();
        category = in.readString();
        subject = in.readString();
        copies = in.readString();
        tag = in.readString();
        imguri = in.readString();
        bookid = in.readString();
    }

    public static final Creator<BookModel> CREATOR = new Creator<BookModel>() {
        @Override
        public BookModel createFromParcel(Parcel in) {
            return new BookModel(in);
        }

        @Override
        public BookModel[] newArray(int size) {
            return new BookModel[size];
        }
    };

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthername() {
        return authername;
    }

    public void setAuthername(String authername) {
        this.authername = authername;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCopies() {
        return copies;
    }

    public void setCopies(String copies) {
        this.copies = copies;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bookname);
        parcel.writeString(authername);
        parcel.writeString(contributors);
        parcel.writeString(publisher);
        parcel.writeString(edition);
        parcel.writeString(ISBN);
        parcel.writeString(category);
        parcel.writeString(subject);
        parcel.writeString(copies);
        parcel.writeString(tag);
        parcel.writeString(imguri);
        parcel.writeString(bookid);
    }
}