package com.coderpakistan.learningbank.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {

    SharedPreferences userDB;
    public SharedPreferences.Editor editor;
    public Context context;
    int private_mode = 0;
    private static final String userDBName = "userData";


    public UserSessionManager(Context context) {
        this.context = context;
        userDB = context.getSharedPreferences(userDBName, private_mode);
    }


    public void setSessionDetails(Session sessionDetails) {
        SharedPreferences.Editor DataDetails = userDB.edit();
        DataDetails.putString("id", sessionDetails.getId());
        DataDetails.putString("name", sessionDetails.getName());
        DataDetails.putString("phoneno", sessionDetails.getPhone());
        DataDetails.putString("email", sessionDetails.getEmail());
        DataDetails.putString("image", sessionDetails.getImage());
        DataDetails.apply();

    }

    public Session getSessionDetails() {
        String id = userDB.getString("id", "");
        String name = userDB.getString("name", "");
        String phoneno = userDB.getString("phoneno", "");
        String email = userDB.getString("email", "");
        String image = userDB.getString("image", "");
        Session sessionDetails = new Session(id, email, name, phoneno,image);
        return sessionDetails;
    }

    public boolean isLoggedIn() {
        return userDB.getBoolean("loggedIn", false);
    }


    public void clearSessionData() {
        SharedPreferences.Editor clientSpEditor = userDB.edit();
        clientSpEditor.clear();
        clientSpEditor.apply();
    }


    public void setLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor riderSpEditor = userDB.edit();
        riderSpEditor.putBoolean("loggedIn", loggedIn);
        riderSpEditor.apply();
    }


}