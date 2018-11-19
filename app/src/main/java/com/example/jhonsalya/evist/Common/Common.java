package com.example.jhonsalya.evist.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.jhonsalya.evist.Model.User;

/**
 * Created by jhonsalya on 19/11/18.
 */

public class Common {
    public static User currentUser;

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "on my way";
        else
            return "Shipped";
    }

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        if(connectivityManager != null)
        {
            for(int i=0;i<info.length;i++)
            {
                if(info[i].getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }
        return false;
    }
}
