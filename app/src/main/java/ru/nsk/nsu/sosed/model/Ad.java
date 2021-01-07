package ru.nsk.nsu.sosed.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ad implements Parcelable {
    private String title;
    private String text;
    private String imageUrl;
    private String author;
    private int topic;
    private Date createdDate;

    public Ad() {
    }

    protected Ad(Parcel in) {
        final String[] data = new String[5];
        in.readStringArray(data);
        title = data[0];
        text = data[1];
        imageUrl = data[2];
        author = data[3];
        try {
            createdDate = SimpleDateFormat.getInstance().parse(data[4]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<Ad> CREATOR = new Creator<Ad>() {
        @Override
        public Ad createFromParcel(Parcel in) {
            return new Ad(in);
        }

        @Override
        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };

    public Ad(String title, String text, String imageUrl, String author, int topic, Date createdDate) {
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.author = author;
        this.topic = topic;
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[] {
                title,
                text,
                imageUrl,
                author,
                SimpleDateFormat.getInstance().format(createdDate)
        });
    }
}
