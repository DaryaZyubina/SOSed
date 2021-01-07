package ru.nsk.nsu.sosed.data.ad;

import ru.nsk.nsu.sosed.data.FirebaseDatabaseRepository;
import ru.nsk.nsu.sosed.model.Ad;

public class AdRepository extends FirebaseDatabaseRepository<Ad> {

    public AdRepository() {
        super(new AdMapper());
    }

    @Override
    protected String getRootNode() {
        return "ads";
    }

    @Override
    public void addChild(Ad ad) {
        databaseReference.push().setValue(new AdMapper().map(ad));
    }
}
