package com.example.fragments;

import com.example.thrifty.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutUsFragment extends Fragment {
	@Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		View v = inflater.inflate(R.layout.about_us, container,false); 
		return v; 
	}
}
