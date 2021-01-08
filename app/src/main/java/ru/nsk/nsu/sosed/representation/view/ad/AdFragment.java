package ru.nsk.nsu.sosed.representation.view.ad;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosed.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ru.nsk.nsu.sosed.model.Ad;
import ru.nsk.nsu.sosed.representation.viewmodel.AdViewModel;
import ru.nsk.nsu.sosed.representation.adapter.AdAdapter;

public class AdFragment extends Fragment {

    private RecyclerView recyclerView;
    private int category;

    private FloatingActionButton addAd;

    public AdFragment(){ }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ads, container, false);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        category = bundle.getInt("ad");
        Log.i("ad fragment", "category: " + category);
        String[] categories = getResources().getStringArray(R.array.ad_categories);
        recyclerView = v.findViewById(R.id.recycler_view_ads);
        addAd = v.findViewById(R.id.add_ad_fab);
        if (category==-1){
            getActivity().setTitle(getString(R.string.title_hcs));
            addAd.setVisibility(View.GONE);
        }
        if (0 < category && category < categories.length) {
            getActivity().setTitle(categories[category]);
        }
        addAd.setOnClickListener(v1 -> {
            Bundle bundle1 = new Bundle();
            bundle1.putInt("ad", category);
            Fragment fragment = new NewAdFragment();
            fragment.setArguments(bundle1);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AdViewModel viewModel = new ViewModelProvider(this).get(AdViewModel.class);
        viewModel.getAds().observe(getViewLifecycleOwner(), ads -> {
            List<Ad> adsFiltered = getAdsFiltered(ads);
            recyclerView.setAdapter(new AdAdapter(adsFiltered));
        });
    }

    private List<Ad> getAdsFiltered(List<Ad> ads){
        int topic = category+1;
        List<Ad> adsFiltered = new ArrayList<>();
        for(Ad a:ads){
            if(a.getTopic()==topic){
                adsFiltered.add(a);
            }
        }
        return adsFiltered;
    }
}
