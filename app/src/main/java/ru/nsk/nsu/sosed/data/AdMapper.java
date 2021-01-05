package ru.nsk.nsu.sosed.data;

import java.util.Date;

import ru.nsk.nsu.sosed.domain.Ad;

public class AdMapper  extends FirebaseMapper<AdEntity, Ad> {

    @Override
    public Ad map(AdEntity adEntity) {
        Ad ad = new Ad();
        ad.setAuthor(adEntity.getAuthorId()); //todo: get author's name from users
        ad.setCreatedDate(new Date(adEntity.getCreatedDate()*1000));
        ad.setImageUrl(adEntity.getImageUrl());
        ad.setTitle(adEntity.getTitle());
        ad.setText(adEntity.getText());
        return ad;
    }
}
