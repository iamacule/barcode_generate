package vn.mran.barcodegenerate.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import vn.mran.barcodegenerate.pref.Constant;

/**
 * Created by AnPham on 05.05.2016.
 * <p>
 * Last modified on 19.01.2017
 * <p>
 * Copyright 2017 Audi AG, All Rights Reserved
 */
public class FileHelper {
    public final String TAG = getClass().getSimpleName();
    private File file = null;

    public FileHelper() {
    }

    public void generateFile(String fileName) {
        File utrFolder = new File(Environment.getExternalStorageDirectory() + Constant.BARCODE_EXPORT_FOLDER_NAME);
        if (!utrFolder.exists()) {
            utrFolder.mkdir();
            Log.d(TAG, "Create BARCODE_EXPORT_FOLDER_NAME folder success");
        }
        file = new File(utrFolder, File.separator + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.d(TAG, "Create new file success : " + fileName);
            } catch (Exception e) {
                Log.d(TAG, "Create new file fail");
            }
        } else {
            Log.d(TAG, "Founded : " + fileName);
        }
    }

    public File getFile() {
        return file;
    }
}
