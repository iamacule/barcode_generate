package vn.mran.barcodegenerate.mvp.presenter;

import android.content.Context;

import vn.mran.barcodegenerate.R;

/**
 * Created by Mr An on 20/09/2017.
 */

public class InputPresenter {
    public interface InputView {
        void checkNumberSuccess(int from, int to);

        void checkNumberFail(String message);
    }

    private Context context;
    private InputView inputView;

    public InputPresenter(Context context, InputView inputView) {
        this.inputView = inputView;
        this.context = context;
    }

    public void checkNumber(String from, String to) {
        try {
            int fromInt = Integer.parseInt(from);
            int toInt = Integer.parseInt(to);
            if (toInt <= fromInt)
                inputView.checkNumberFail(context.getString(R.string.input_min_wrong));
            else inputView.checkNumberSuccess(fromInt, toInt);
        } catch (Exception e) {
            inputView.checkNumberFail(context.getString(R.string.input_empty));
        }
    }
}
