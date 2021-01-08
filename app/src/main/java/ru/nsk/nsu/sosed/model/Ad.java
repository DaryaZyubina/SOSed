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
    private String authorName;
    private String authorId;
    private int topic;
    private Date createdDate;

    public Ad() {
    }

    protected Ad(Parcel in) {
        final String[] data = new String[7];
        in.readStringArray(data);
        title = data[0];
        text = data[1];
        imageUrl = data[2];
        authorName = data[3];
        authorId = data[4];
        topic = Integer.parseInt(data[5]);
        try {
            createdDate = SimpleDateFormat.getInstance().parse(data[6]);
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

    public Ad(String title, String text, String imageUrl, String authorName, String authorId, int topic, Date createdDate) {
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.authorName = authorName;
        this.authorId = authorId;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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
                authorName,
                authorId,
                Integer.toString(topic),
                SimpleDateFormat.getInstance().format(createdDate)
        });
    }


}
