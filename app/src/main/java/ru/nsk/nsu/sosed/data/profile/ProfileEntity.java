package ru.nsk.nsu.sosed.data.profile;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProfileEntity {
    private String name;
    private String email;
    private String apartment_num;
    private String house;
    private Boolean messagingEnabled;
    private String imageUrl;


    public ProfileEntity() {
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

    public String getApartment_num() {
        return apartment_num;
    }

    public void setApartment_num(String apartment_num) {
        this.apartment_num = apartment_num;
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
