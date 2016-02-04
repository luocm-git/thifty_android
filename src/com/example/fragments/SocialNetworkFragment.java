package com.example.fragments;

import com.example.thrifty.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SocialNetworkFragment extends Fragment {
	@Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		View v = inflater.inflate(R.layout.social_media, container,false); 
		return v; 
	}
}
