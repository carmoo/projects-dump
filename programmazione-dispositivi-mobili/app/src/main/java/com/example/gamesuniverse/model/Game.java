package com.example.gamesuniverse.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class Game implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String slug;
    private String released;
    private boolean tba;
    private String background_image;
    private double rating;
    private int metacritic;
    private int playtime;
    private String updated;
    public Game() {
    }

    public Game(long id, String name, String slug, String released, boolean tba, String background_image, double rating,
                int metacritic, int playtime, String updated) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.released = released;
        this.tba = tba;
        this.background_image = background_image;
        this.rating = rating;
        this.metacritic = metacritic;
        this.playtime = playtime;
        this.updated = updated;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getReleased() {
        return released;
    }

    public boolean isTba() {
        return tba;
    }

    public String getBackground_image() {
        return background_image;
    }

    public double getRating() {
        return rating;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public int getPlaytime() {
        return playtime;
    }

    public String getUpdated() {
        return updated;
    }
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setTba(boolean tba) {
        this.tba = tba;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", released='" + released + '\'' +
                ", tba=" + tba +
                ", background_image='" + background_image + '\'' +
                ", rating=" + rating +
                ", metacritic=" + metacritic +
                ", playtime=" + playtime +
                ", updated='" + updated + '\'' +
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
        parcel.writeString(slug);
        parcel.writeString(released);
        parcel.writeByte(this.tba ? (byte) 1 : (byte) 0);
        parcel.writeString(background_image);
        parcel.writeDouble(rating);
        parcel.writeInt(metacritic);
        parcel.writeInt(playtime);
        parcel.writeString(updated);
    }

    public void readFromParcel(Parcel source){
        this.id = source.readInt();
        this.name = source.readString();
        this.slug = source.readString();
        this.released = source.readString();
        this.tba = source.readByte() != 0;
        this.background_image = source.readString();
        this.rating = source.readDouble();
        this.metacritic = source.readInt();
        this.playtime = source.readInt();
        this.updated = source.readString();
    }

    protected Game(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.slug = in.readString();
        this.released = in.readString();
        this.tba = in.readByte() != 0;
        this.background_image = in.readString();
        this.rating = in.readDouble();
        this.metacritic = in.readInt();
        this.playtime = in.readInt();
        this.updated = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
