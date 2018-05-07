package vn.mran.barcodegenerate.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import vn.mran.barcodegenerate.R;
import vn.mran.barcodegenerate.base.BaseFragment;
import vn.mran.barcodegenerate.mvp.presenter.InputPresenter;
import vn.mran.barcodegenerate.utils.TouchEffect;

/**
 * Created by Mr An on 20/09/2017.
 */

public class InputFragment extends BaseFragment implements View.OnClickListener, InputPresenter.InputView {
    private LinearLayout btnNext;
    private EditText edtFrom;
    private EditText edtTo;
    private InputPresenter presenter;

    @Override
    public void initView() {
        btnNext = (LinearLayout) view.findViewById(R.id.btnNext);
        edtFrom = (EditText) view.findViewById(R.id.edtFrom);
        edtTo = (EditText) view.findViewById(R.id.edtTo);

        TouchEffect.addAlpha(btnNext);
    }

    @Override
    public void initValue() {
        presenter = new InputPresenter(context, this);
    }

    @Override
    public void initAction() {
        btnNext.setOnClickListener(this);
    }

    @Override
    public int setFragmentView() {
        return R.layout.fragment_input;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                presenter.checkNumber(
                        edtFrom.getText().toString().trim(),
                        edtTo.getText().toString().trim());
                break;
        }
    }

    @Override
    public void checkNumberSuccess(int from, int to) {
        goTo(new LogoFragment());
        ((LogoFragment) getActiveFragment()).setData(from, to);
    }

    @Override
    public void checkNumberFail(String message) {
        showDialogInfo(
                context.getString(R.string.dialog_error_title),
                message);
    }
}
