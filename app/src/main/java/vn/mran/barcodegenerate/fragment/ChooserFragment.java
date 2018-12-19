package vn.mran.barcodegenerate.fragment;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import vn.mran.barcodegenerate.R;
import vn.mran.barcodegenerate.base.BaseFragment;
import vn.mran.barcodegenerate.utils.TouchEffect;

/**
 * Created by Mr An on 20/09/2017.
 */

public class ChooserFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private LinearLayout btnSpecialize;
    private LinearLayout btnNews;

    @Override
    public void initView() {
        btnSpecialize = (LinearLayout) view.findViewById(R.id.btnSpecialize);
        btnNews = (LinearLayout) view.findViewById(R.id.btnNews);

        TouchEffect.addAlpha(btnSpecialize);
        TouchEffect.addAlpha(btnNews);
    }

    @Override
    public void initValue() {
    }

    @Override
    public void initAction() {
        btnSpecialize.setOnClickListener(this);
        btnNews.setOnClickListener(this);
    }

    @Override
    public int setFragmentView() {
        return R.layout.fragment_chooser;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSpecialize:
                Log.d(TAG, "btnSpecialize");
                goTo(new SpecializeFragment());
                break;
            case R.id.btnNews:
                Log.d(TAG, "btnNews");
                goTo(new InputFragment());
                break;
        }
    }
}
