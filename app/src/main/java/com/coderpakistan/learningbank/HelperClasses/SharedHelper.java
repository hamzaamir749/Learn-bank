package com.coderpakistan.learningbank.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {
    SharedPreferences storeinfo;
    public SharedPreferences.Editor editor;
    public Context context;
    int private_mode = 0;

    public SharedHelper(Context context) {
        this.context = context;
        storeinfo = context.getSharedPreferences(storeDBName, private_mode);
    }

    private static final String storeDBName = "storeinfo"; //userData
    public void putKey(String key,String value) {
        SharedPreferences.Editor DataDetails=storeinfo.edit();
        DataDetails.putString(key,value);
        DataDetails.apply();
    }

    public String getKey(String key){
        storeinfo = context.getSharedPreferences(storeDBName, private_mode);
        String keyData=storeinfo.getString(key ,"");
        return keyData;
    }

    public void clearStoreInfoData() {
        SharedPreferences.Editor clientSpEditor = storeinfo.edit();
        clientSpEditor.clear();
        clientSpEditor.apply();
    }
}
