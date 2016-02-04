package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import com.example.thrifty.R;
import com.example.thrifty.StoreActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ViewSwitcher;

public class BranchFragment extends Fragment {

   private ListView cities_list_view, branches_list_view;
   ViewSwitcher viewSwitcher;

   Animation slide_in_left, slide_out_right;
   
   ParseObject cities[], branch_offices[];
   
   public BranchFragment(){}
    
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
       View rootView = inflater.inflate(R.layout.cities_list, container, false);
       
       viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.viewswitcher);

       slide_in_left = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
       slide_out_right = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);

       viewSwitcher.setInAnimation(slide_in_left);
       viewSwitcher.setOutAnimation(slide_out_right);

		cities_list_view = (ListView) rootView.findViewById(R.id.cities_list);
		branches_list_view = (ListView) rootView.findViewById(R.id.branches_office_list);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("state");
		query.orderByAscending("name");
		query.whereEqualTo("objectId","fzSbypmk8u");
		
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> object, ParseException excp) {
				if (object != null) {
					for(ParseObject state : object){
						
						ParseQuery<ParseObject> query = ParseQuery.getQuery("city");
						query.orderByAscending("name");
						query.whereEqualTo("state",state);
						
						query.findInBackground(new FindCallback<ParseObject>() {
							@Override
							public void done(List<ParseObject> object, ParseException excp) {
								int i = 0;
								cities = new ParseObject[object.size()];
								List<String> cities_array = new ArrayList<String>();
								for(ParseObject city : object){
									cities[i] = city;
									cities_array.add(city.getString("name"));
									i++;
								}	
								
								ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
									getActivity(), 
								    android.R.layout.simple_list_item_1,
								    cities_array );
									cities_list_view.setAdapter(arrayAdapter);	
									
									cities_list_view.setOnItemClickListener(new OnItemClickListener() {
								        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
								        	ParseQuery<ParseObject> query = ParseQuery.getQuery("branch_office");
											query.whereEqualTo("city",cities[position]);
											query.orderByAscending("Name");
											
											query.findInBackground(new FindCallback<ParseObject>() {
												@Override
												public void done(List<ParseObject> object, ParseException excp) {
													int i = 0;
													branch_offices = new ParseObject[object.size()];
													List<String> branch_array = new ArrayList<String>();
													
													for(ParseObject branch : object){
														branch_offices[i] = branch;
														branch_array.add(branch.getString("Name"));
														i++;
													}					
													ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
														getActivity(), 
													    android.R.layout.simple_list_item_1,
													    branch_array );
													
														branches_list_view.setAdapter(arrayAdapter);	
														branches_list_view.setOnItemClickListener(new OnItemClickListener() {
													        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
													        	
													        	Intent i = new Intent(getActivity(),StoreActivity.class);
													        	i.putExtra("objectId",branch_offices[position].getObjectId());
													        	getActivity().startActivity(i);
													        }
													    });
												}
											});
								        	viewSwitcher.showPrevious();
								        }
								    });
							}
						});
					}
			    } else {
			      excp.printStackTrace();
			    }			
			}
		});
       return rootView;
   }

}
