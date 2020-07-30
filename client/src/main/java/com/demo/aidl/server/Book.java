package com.demo.aidl.server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guoxiaodong on 2019-11-13 15:34
 */
public class Book implements Parcelable {
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    public String name;

    public Book() {
    }

    public Book(String name) {
        this.name = name;
    }

    protected Book(Parcel in) {
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel dest) {
        this.name = dest.readString();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, hashCode() % 1000);
    }
}
