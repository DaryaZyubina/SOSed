package ru.nsk.nsu.sosed.data.profile;

import ru.nsk.nsu.sosed.data.FirebaseDatabaseRepository;
import ru.nsk.nsu.sosed.data.ad.AdMapper;
import ru.nsk.nsu.sosed.model.Ad;

public class ProfileRepository extends FirebaseDatabaseRepository<ProfileEntity> {

    public ProfileRepository() {
        super();
    }

    @Override
    protected String getRootNode() {
        return "users";
    }

    @Override
    public void addChild(ProfileEntity profile) {
        databaseReference.child(profile.getUid()).setValue(profile);
    }
}