package ru.nsk.nsu.sosed.representation.Message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sosed.R;

public class MessageFragment extends Fragment {

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_icon_message));


        return v;
    }

}
