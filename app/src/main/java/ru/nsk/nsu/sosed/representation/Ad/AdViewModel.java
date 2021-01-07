package ru.nsk.nsu.sosed.representation.Ad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.nsk.nsu.sosed.data.ad.AdRepository;
import ru.nsk.nsu.sosed.data.FirebaseDatabaseRepository;
import ru.nsk.nsu.sosed.model.Ad;

public class AdViewModel extends ViewModel {
    private MutableLiveData<List<Ad>> ads;
    private AdRepository repository = new AdRepository();

    public LiveData<List<Ad>> getAds() {
        if (ads == null) {
            ads = new MutableLiveData<>();
            loadArticles();
        }
        return ads;
    }

    @Override
    protected void onCleared() {
        repository.removeListener();
    }

    private void loadArticles() {
        repository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Ad>() {
            @Override
            public void onSuccess(List<Ad> result) {
                ads.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                ads.setValue(null);
            }
        });
    }

    public void saveAd(Ad ad){
        repository.addChild(ad);
    }
}
