package ru.nsk.nsu.sosed.representation.Auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.nsk.nsu.sosed.data.FirebaseDatabaseRepository;
import ru.nsk.nsu.sosed.data.ad.AdRepository;
import ru.nsk.nsu.sosed.data.profile.ProfileEntity;
import ru.nsk.nsu.sosed.data.profile.ProfileRepository;
import ru.nsk.nsu.sosed.model.Ad;

public class AuthViewModel extends ViewModel {
    private ProfileRepository repository = new ProfileRepository();

    @Override
    protected void onCleared() {
        repository.removeListener();
    }

    public void saveProfile(ProfileEntity profile){
        repository.addChild(profile);
    }
}