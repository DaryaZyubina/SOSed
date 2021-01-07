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
        ad.setTopic(adEntity.getTopic());
        return ad;
    }
    public AdEntity map(Ad ad){
        AdEntity adEntity = new AdEntity();
        adEntity.setAuthorId(ad.getAuthor());
        adEntity.setCreatedDate(ad.getCreatedDate().getTime()/1000);
        adEntity.setImageUrl(ad.getImageUrl());
        adEntity.setTitle(ad.getTitle());
        adEntity.setText(ad.getText());
        adEntity.setTopic(ad.getTopic());
        return adEntity;
    }
}
