package ru.nsk.nsu.sosed.data;

import ru.nsk.nsu.sosed.domain.Ad;

public class AdRepository extends FirebaseDatabaseRepository<Ad> {

    public AdRepository() {
        super(new AdMapper());
    }

    @Override
    protected String getRootNode() {
        return "ads";
    }
}
