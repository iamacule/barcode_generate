package vn.mran.barcodegenerate.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * Created by AnPham on 05.05.2016.
 * <p>
 * Last modified on 19.01.2017
 * <p>
 * Copyright 2017 Audi AG, All Rights Reserved
 */
public class PermissionHelper {
    private final String FIRST_TIME_ACCOUNT_KEY = "FIRST_TIME_ACCOUNT_KEY";

    private Activity activity;
    private SharedPreferences preferences;

    public PermissionHelper(Activity activity) {
        this.activity = activity;
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void requestPermission(String permission, int idCallBack) {
        boolean firstTimeAccount = preferences.getBoolean(FIRST_TIME_ACCOUNT_KEY, true);

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // 2. Asked before, and the user said "no"
            ActivityCompat.requestPermissions(activity, new String[]{permission}, idCallBack);
        } else {
            if (firstTimeAccount) {
                // 1. first time, never asked
                preferences.edit().putBoolean(FIRST_TIME_ACCOUNT_KEY, false).apply();
                // Account permission has not been granted, request it directly.
                ActivityCompat.requestPermissions(activity, new String[]{permission}, idCallBack);
            } else {
                // 3. If you asked a couple of times before, and the user has said "no, and stop asking"
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }
    }

    public boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED ? true : false;
    }
}
