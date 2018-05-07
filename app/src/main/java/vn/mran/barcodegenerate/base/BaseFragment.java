package vn.mran.barcodegenerate.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.mran.barcodegenerate.MainActivity;
import vn.mran.barcodegenerate.R;
import vn.mran.barcodegenerate.dialog.DialogInfo;

/**
 * Created by Mr An on 20/09/2017.
 */

public abstract class BaseFragment extends Fragment implements Init {

    protected Context context;
    protected View view;
    private DialogInfo.Build dialogInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setFragmentView(), container, false);
        context = getActivity();
        initBaseValue();

        // init value
        initView();
        initValue();
        initAction();

        return view;
    }

    private void initBaseValue() {
        view.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialogInfo = new DialogInfo.Build(getActivity());
    }

    // get view
    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public abstract int setFragmentView();

    protected MainActivity getMainActivity() {
        return ((MainActivity) getActivity());
    }

    protected void onBackPressed() {
        getMainActivity().onBackPressed();
    }

    protected void goTo(BaseFragment baseFragment) {
        getMainActivity().goTo(baseFragment);
    }

    protected void showDialogInfo(String title, String message) {
        dialogInfo.setTitle(title).setMessage(message).show();
    }

    protected Fragment getActiveFragment(){
        return getMainActivity().getActiveFragment();
    }
}

