package ru.zhdanov.advicer.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private Context context;

    public Utils(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        ed = sp.edit();
    }

    public String getLogin() {
        return sp.getString(Constants.USER_LOGIN, "");
    }

    public void setLogin(String login) {
        ed.putString(Constants.USER_LOGIN, login);
        ed.apply();
    }

    public boolean getPushSwitch() {
        return sp.getBoolean(Constants.PUSH_SWITCH, true);
    }

    public void setPushSwitch(boolean push) {
        ed.putBoolean(Constants.PUSH_SWITCH, push);
        ed.apply();
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public int nowhour() {
        return Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
    }
}
