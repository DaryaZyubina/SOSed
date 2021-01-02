package com.example.sosed.HCS;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sosed.R;

public class HCSFragment  extends Fragment {

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hcs, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_hcs));


        return v;
    }

}
