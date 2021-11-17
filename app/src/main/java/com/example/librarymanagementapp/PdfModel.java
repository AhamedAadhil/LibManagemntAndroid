package com.example.librarymanagementapp;

import android.os.Parcel;
import android.os.Parcelable;

public class PdfModel implements Parcelable {
String imguri,pdfuri,name,category,subject;

  public PdfModel(){

  }

    public PdfModel(String imguri, String pdfuri, String name, String category, String subject) {
        this.imguri = imguri;
        this.pdfuri = pdfuri;
        this.name = name;
        this.category = category;
        this.subject = subject;
    }

    protected PdfModel(Parcel in) {
        imguri = in.readString();
        pdfuri = in.readString();
        name = in.readString();
        category = in.readString();
        subject = in.readString();
    }

    public static final Creator<PdfModel> CREATOR = new Creator<PdfModel>() {
        @Override
        public PdfModel createFromParcel(Parcel in) {
            return new PdfModel(in);
        }

        @Override
        public PdfModel[] newArray(int size) {
            return new PdfModel[size];
        }
    };

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public String getPdfuri() {
        return pdfuri;
    }

    public void setPdfuri(String pdfuri) {
        this.pdfuri = pdfuri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imguri);
        parcel.writeString(pdfuri);
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeString(subject);
    }
}
