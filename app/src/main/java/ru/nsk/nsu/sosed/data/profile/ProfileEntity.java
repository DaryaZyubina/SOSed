package ru.nsk.nsu.sosed.data.profile;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProfileEntity {

    private String uid;
    private String name;
    private String email;
    private String apartmentNum;
    private String house;
    private Boolean messagingEnabled;
    private String imageUrl;


    public ProfileEntity() {
    }

    public ProfileEntity(String uid, String name, String email, String apartmentNum, String house, Boolean messagingEnabled, String imageUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.apartmentNum = apartmentNum;
        this.house = house;
        this.messagingEnabled = messagingEnabled;
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getApartmentNum() {
        return apartmentNum;
    }

    public void setApartmentNum(String apartmentNum) {
        this.apartmentNum = apartmentNum;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public Boolean getMessagingEnabled() {
        return messagingEnabled;
    }

    public void setMessagingEnabled(Boolean messagingEnabled) {
        this.messagingEnabled = messagingEnabled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
