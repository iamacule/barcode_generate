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

public class SpecializeFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private LinearLayout btnAdd;
    private LinearLayout btnDelete;
    private LinearLayout btnNext;

    @Override
    public void initView() {
        btnAdd = (LinearLayout) view.findViewById(R.id.btnAdd);
        btnDelete = (LinearLayout) view.findViewById(R.id.btnDelete);
        btnNext = (LinearLayout) view.findViewById(R.id.btnNext);

        TouchEffect.addAlpha(btnAdd);
        TouchEffect.addAlpha(btnDelete);
        TouchEffect.addAlpha(btnNext);
    }

    @Override
    public void initValue() {
    }

    @Override
    public void initAction() {
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public int setFragmentView() {
        return R.layout.fragment_specialize;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(TAG, "btnAdd");
                break;
            case R.id.btnDelete:
                Log.d(TAG, "btnDelete");
                break;
            case R.id.btnNext:
                Log.d(TAG, "btnNext");
                break;
        }
    }
}
