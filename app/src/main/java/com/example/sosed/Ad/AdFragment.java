package com.example.sosed.Ad;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sosed.R;

public class AdFragment extends Fragment {

    public AdFragment(){ }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ad, container, false);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        String data = bundle.getString("ad");

        getActivity().setTitle(data);


        return v;
    }
}
