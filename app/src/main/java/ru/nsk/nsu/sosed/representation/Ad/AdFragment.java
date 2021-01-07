package ru.nsk.nsu.sosed.representation.Ad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosed.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ru.nsk.nsu.sosed.domain.Ad;

public class AdFragment extends Fragment {

    private RecyclerView recyclerView;
    private String category;

    private FloatingActionButton addAd;

    public AdFragment(){ }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ads, container, false);

        recyclerView = v.findViewById(R.id.recycler_view_ads);
        addAd = v.findViewById(R.id.add_ad_fab);
        addAd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewAdActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = this.getArguments();
        assert bundle != null;
        category = bundle.getString("ad");
        Log.i("ad fragment", "category: " + category);

        getActivity().setTitle(category);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AdViewModel viewModel = new ViewModelProvider(this).get(AdViewModel.class);
        viewModel.getAds().observe(getViewLifecycleOwner(), new Observer<List<Ad>>() {
            @Override
            public void onChanged(@Nullable List<Ad> ads) {
                List<Ad> adsFiltered = getAdsFiltered(ads);
                recyclerView.setAdapter(new AdAdapter(adsFiltered));
            }
        });
    }

    private List<Ad> getAdsFiltered(List<Ad> ads){ //todo: перенести во viewModel?
        String[] categories = getResources().getStringArray(R.array.ad_categories);
        int topic=-1;
        if(category.equals("ЖКХ")){
            topic = 0;
        }
        for(int i=0; i<categories.length; i++){
            if (categories[i].equals(category)){
                topic = i+1;
            }
        }
        List<Ad> adsFiltered = new ArrayList<Ad>();
        for(Ad a:ads){
            if(a.getTopic()==topic){
                adsFiltered.add(a);
            }
        }
        return adsFiltered;
    }
}
