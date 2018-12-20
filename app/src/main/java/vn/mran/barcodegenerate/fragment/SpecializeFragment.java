package vn.mran.barcodegenerate.fragment;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.mran.barcodegenerate.R;
import vn.mran.barcodegenerate.base.BaseFragment;
import vn.mran.barcodegenerate.dialog.DialogInput;
import vn.mran.barcodegenerate.utils.TouchEffect;

/**
 * Created by Mr An on 20/09/2017.
 */

public class SpecializeFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private LinearLayout btnAdd;
    private LinearLayout btnDelete;
    private LinearLayout btnNext;
    private TextView txtShowing;

    private List<Integer> listSpecialize = new ArrayList<>();

    @Override
    public void initView() {
        btnAdd = (LinearLayout) view.findViewById(R.id.btnAdd);
        btnDelete = (LinearLayout) view.findViewById(R.id.btnDelete);
        btnNext = (LinearLayout) view.findViewById(R.id.btnNext);
        txtShowing = (TextView) view.findViewById(R.id.txtShowing);

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
                new DialogInput.Build(getActivity())
                        .setTitle(getString(R.string.input_specialize))
                        .setOnDialogInputListener(new DialogInput.OnDialogInputListener() {
                            @Override
                            public void onNumberReturn(int number) {
                                listSpecialize.add(number);
                                initShowing();
                            }
                        }).show();
                break;
            case R.id.btnDelete:
                Log.d(TAG, "btnDelete");
                try {
                    listSpecialize.remove(listSpecialize.get(listSpecialize.size() - 1));
                    initShowing();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.delete_fail), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnNext:
                Log.d(TAG, "btnNext");
                if (listSpecialize.size() > 0) {

                    goTo(new LogoFragment());
                    ((LogoFragment) getActiveFragment()).setData(0, listSpecialize.size() - 1, listSpecialize);
                }
                break;
        }
    }

    private void initShowing() {
        StringBuilder showingText = new StringBuilder();
        for (Integer s : listSpecialize) {
            showingText.append((String.format("%07d",s)) + " ");
        }
        txtShowing.setText(showingText.toString().replaceAll("\\s+", System.getProperty("line.separator")));
    }
}
