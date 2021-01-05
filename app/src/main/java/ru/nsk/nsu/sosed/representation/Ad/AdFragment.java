package ru.nsk.nsu.sosed.representation.Ad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosed.R;

import java.util.List;

import ru.nsk.nsu.sosed.domain.Ad;

public class AdFragment extends Fragment {

    private RecyclerView recyclerView;

    public AdFragment(){ }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ads, container, false);

        recyclerView = v.findViewById(R.id.recycler_view_ads);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        String data = bundle.getString("ad");

        getActivity().setTitle(data);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AdViewModel viewModel = new ViewModelProvider(this).get(AdViewModel.class);
        viewModel.getAds().observe(getViewLifecycleOwner(), new Observer<List<Ad>>() {
            @Override
            public void onChanged(@Nullable List<Ad> articles) {
                recyclerView.setAdapter(new AdAdapter(articles));
            }
        });
    }
}
