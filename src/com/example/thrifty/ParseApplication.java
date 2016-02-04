package com.example.thrifty;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "PJIDGeKA5hEYJL24U0hHuClCVt6G5tjsqjC1elHv";
    public static final String YOUR_CLIENT_KEY = "ha2cQDOWPZcy0Onlf3cforjf6Dnj7wZjzH8iLJt5";

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        
        ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
 
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
 
		ParseACL.setDefaultACL(defaultACL, true);
  }		
}