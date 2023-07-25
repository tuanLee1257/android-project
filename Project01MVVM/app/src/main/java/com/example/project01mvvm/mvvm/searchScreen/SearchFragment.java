package com.example.project01mvvm.mvvm.searchScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project01mvvm.R;
import com.example.project01mvvm.databinding.FragmentSearchBinding;


public class SearchFragment extends Fragment {
    FragmentSearchBinding searchScreenBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchScreenBinding = FragmentSearchBinding.inflate(inflater);

        return searchScreenBinding.getRoot();
    }
}