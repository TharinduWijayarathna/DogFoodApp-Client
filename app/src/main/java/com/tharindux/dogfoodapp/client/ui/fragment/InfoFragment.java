package com.tharindux.dogfoodapp.client.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tharindux.dogfoodapp.client.R;

public class InfoFragment extends Fragment {
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.tharindux.dogfoodapp.client.R.layout.fragment_info, container, false);
    }

}
