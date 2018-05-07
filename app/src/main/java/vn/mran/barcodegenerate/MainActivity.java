package vn.mran.barcodegenerate;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import vn.mran.barcodegenerate.base.BaseFragment;
import vn.mran.barcodegenerate.fragment.LogoFragment;
import vn.mran.barcodegenerate.mvp.presenter.MainPresenter;
import vn.mran.barcodegenerate.pref.Constant;

public class MainActivity extends AppCompatActivity implements MainPresenter.MainView {
    private final String TAG = getClass().getSimpleName();
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
    }

    public void onBackPressed() {
        mainPresenter.onBackPressed();
    }

    public void goTo(BaseFragment baseFragment) {
        mainPresenter.goTo(baseFragment);
    }

    public Fragment getActiveFragment() {
        return mainPresenter.getActiveFragment();
    }

    @Override
    public void moveTaskToBack() {
        moveTaskToBack(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Constant.ID_CALLBACK_WRITE_EXTERNAL_STORAGE_PERMISSION:
                ((LogoFragment)getActiveFragment()).onPermissionGranted();
                break;
        }
    }
}
