package com.example.gamesuniverse.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Genre implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String image_background;

    public Genre() {
    }

    public Genre(long id, String name, String image_background) {
        this.id = id;
        this.name = name;
        this.image_background = image_background;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_background() {
        return image_background;
    }

    public void setImage_background(String image_background) {
        this.image_background = image_background;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image_background='" + image_background + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(image_background);
    }

    public void readFromParcel(Parcel source){
        this.id = source.readLong();
        this.name = source.readString();
        this.image_background = source.readString();
    }

    protected Genre (Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.image_background = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
